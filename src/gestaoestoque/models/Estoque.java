package gestaoestoque.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import gestaoestoque.config.DatabaseConfig;
import gestaoestoque.utils.DialogUtils;

public class Estoque extends BaseModel {
	private int id;
	private int produtoId;
	private Produto produto;
	private int quantidade;
	private int quantidadeMinima;
	private int quantidadeMaxima;
	private int pontoReposicao;
	private String status;
	
	@Override
	public String getNomeTabela() {
		return "estoques";
	}
	
	public Estoque() {}

	public Estoque(int id, int produtoId, int quantidade, int quantidadeMinima, int quantidadeMaxima, int pontoReposicao, String status) {
		super();
		this.id = id;
		this.produtoId = produtoId;
		this.quantidade = quantidade;
		this.quantidadeMinima = quantidadeMinima;
		this.quantidadeMaxima = quantidadeMaxima;
		this.pontoReposicao = pontoReposicao;
		this.status = status;
	}

	public int getId() {
		return id;
	}
	
	
	public int getProdutoId() {
		return produtoId;
	}

	public void setProdutoId(int produtoId) {
		this.produtoId = produtoId;
	}

	public Produto getProduto() {
		try {
			String nomeTabelaProduto = new Produto().getNomeTabela();
			String sql = "SELECT * FROM " + nomeTabelaProduto + " WHERE codigo = ?";
			PreparedStatement statement = DatabaseConfig.conexao.prepareStatement(sql);
			statement.setInt(1, produtoId);
			
			ResultSet result = statement.executeQuery();
			
			while(result.next()) {
				int codigo = result.getInt("codigo");
				String nome = result.getString("nome");
				String categoria = result.getString("categoria");
				int fornecedorId = result.getInt("fornecedor_id");
				String unidadeMedida = result.getString("unidade_medida");
				double precoCompra = result.getDouble("preco_compra");
				double precoVenda = result.getDouble("preco_venda");
				String descricao = result.getString("descricao");
				String codigoBarra = result.getString("codigo_barra");
				produto = new Produto(codigo, nome, categoria, fornecedorId, unidadeMedida, precoCompra, precoVenda, descricao, codigoBarra);
			}
			
			return produto;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public int getQuantidadeMinima() {
		return quantidadeMinima;
	}

	public void setQuantidadeMinima(int quantidadeMinima) {
		this.quantidadeMinima = quantidadeMinima;
	}

	public int getQuantidadeMaxima() {
		return quantidadeMaxima;
	}

	public void setQuantidadeMaxima(int quantidadeMaxima) {
		this.quantidadeMaxima = quantidadeMaxima;
	}
	
	public int getPontoReposicao() {
		return pontoReposicao;
	}
	
	public void setPontoReposicao(int pontoReposicao) {
		this.pontoReposicao = pontoReposicao;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public void inserir() throws Exception {
		Estoque isEstoqueExists = Estoque.procurarPeloProdutoId(produtoId);
		if(isEstoqueExists != null) 
			throw new Exception("Produto id ja existe");
		
		String sql = "INSERT INTO " + getNomeTabela() 
				+ "(produto_id, quantidade, quantidade_minima, quantidade_maxima, ponto_reposicao, status)"
				+ "VALUES (?, ?, ?, ?, ?, ?)";
		PreparedStatement statement = DatabaseConfig.conexao.prepareStatement(sql);
		
		statement.setInt(1, produtoId);
		statement.setInt(2, quantidade);
		statement.setInt(3, quantidadeMinima);
		statement.setInt(4, quantidadeMaxima);
		statement.setInt(5, pontoReposicao);
		statement.setString(6, status);
		
        int adicionado = statement.executeUpdate();
        
        if (adicionado > 0) {
            System.out.println("Registro inserido com sucesso!");
        } else {
        	System.out.println("Falha ao inserir registro");
        }
	}
	
	@Override
	public void deletar() {
		try {
			String sql = "DELETE FROM " + getNomeTabela() + " WHERE id = ?";
			PreparedStatement statement = DatabaseConfig.conexao.prepareStatement(sql);
			statement.setInt(1, id);
            int linhasAfetadas = statement.executeUpdate();
            
            if (linhasAfetadas > 0) {
                System.out.println("Registro deletado com sucesso!");
            } else {
                System.out.println("Nenhum registro encontrado com esse ID.");
            }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean salvar() {
		try {
			String status = quantidadeMinima >= quantidade ? "Quase sem estoque" : pontoReposicao >= quantidade ? "Precisa repor" : quantidadeMaxima < quantidade ? "Excesso no estoque" : "Estavel" ; 

			String sql = "UPDATE " + getNomeTabela() + " SET quantidade = ?, quantidade_minima = ?, quantidade_maxima = ?, ponto_reposicao = ?, status = ? WHERE id = ?";
			PreparedStatement stmt;
			stmt = DatabaseConfig.conexao.prepareStatement(sql);
			stmt.setInt(1, quantidade);
			stmt.setInt(2, quantidadeMinima);
			stmt.setInt(3, quantidadeMaxima);
			stmt.setInt(4, pontoReposicao);
			stmt.setString(5, status);
			stmt.setInt(6, id);
			
			int isAtualizou = stmt.executeUpdate();
			
			return isAtualizou > 0; 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
				
	}
	
	public static Estoque procurarPeloProdutoId(int produtoId) {
		try {
			String nomeTabelaEstoque = new Estoque().getNomeTabela();
			String sql = "SELECT * FROM " + nomeTabelaEstoque + " WHERE produto_id = ?";
			PreparedStatement stmt =  DatabaseConfig.conexao.prepareStatement(sql);
			stmt.setInt(1, produtoId);
			ResultSet result = stmt.executeQuery();
			Estoque estoque = null;
			while(result.next()) {
				estoque = new Estoque(result.getInt("id"), result.getInt("produto_id"), result.getInt("quantidade"),
						result.getInt("quantidade_minima"), result.getInt("quantidade_maxima"), result.getInt("ponto_reposicao"),
						result.getString("status"));
			}
			return estoque;
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ArrayList<Estoque> carregarTodosEstoques() {
		ArrayList<Estoque> estoques = new ArrayList<Estoque>();
		
		try {
			String nomeTabelaEstoque = new Estoque().getNomeTabela();
			String sql = "SELECT * FROM " + nomeTabelaEstoque;
			
			PreparedStatement statement = DatabaseConfig.conexao.prepareStatement(sql);
			ResultSet result = statement.executeQuery();
			
			while(result.next()) {
				Estoque estoque = new Estoque(result.getInt("id"), result.getInt("produto_id"), result.getInt("quantidade"),
						result.getInt("quantidade_minima"), result.getInt("quantidade_maxima"), result.getInt("ponto_reposicao"),
						result.getString("status"));
				
				estoques.add(estoque);
			}
			
			return estoques;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
