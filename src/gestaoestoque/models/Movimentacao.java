package gestaoestoque.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import gestaoestoque.config.DatabaseConfig;

public class Movimentacao extends BaseModel {
	int id;
	int produtoId;
	int quantidade;
	String local;
	String tipoLocal;
	Produto produto;
	
	@Override
	public String getNomeTabela() {
		return "movimentacoes";
	}
	
	public Movimentacao() {}
	public Movimentacao(int id, int produtoId, int quantidade, String local, String tipoLocal) {
		this.id = id;
		this.produtoId = produtoId;
		this.quantidade = quantidade;
		this.local = local;
		this.tipoLocal = tipoLocal;
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
	public int getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	public String getLocal() {
		return local;
	}
	public void setLocal(String local) {
		this.local = local;
	}
	public String getTipoLocal() {
		return tipoLocal;
	}
	public void setTipoLocal(String tipoLocal) {
		this.tipoLocal = tipoLocal;
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
	
	@Override
	public void inserir() throws Exception {
		String sql = "INSERT INTO " + getNomeTabela() 
		+ "(produto_id, quantidade, local, tipo_local)"
		+ "VALUES (?, ?, ?, ?)";
		PreparedStatement statement = DatabaseConfig.conexao.prepareStatement(sql);
		
		statement.setInt(1, produtoId);
		statement.setInt(2, quantidade);
		statement.setString(3, local);
		statement.setString(4, tipoLocal);
		
		int adicionado = statement.executeUpdate();
		
		if (adicionado > 0) {
		    System.out.println("Registro inserido com sucesso!");
		} else {
			System.out.println("Falha ao inserir registro");
		}
	}
	
	@Override
	public void deletar() throws Exception {
		String sql = "DELETE FROM " + getNomeTabela() + " WHERE id = ?";
		PreparedStatement statement = DatabaseConfig.conexao.prepareStatement(sql);
		statement.setInt(1, id);
        int linhasAfetadas = statement.executeUpdate();
        
        if (linhasAfetadas > 0) {
            System.out.println("Registro deletado com sucesso!");
        } else {
            System.out.println("Nenhum registro encontrado com esse ID.");
        }
	}
	
	public static ArrayList<Movimentacao> carregarTodasMovimentacoes() {
		ArrayList<Movimentacao> movimentacoes = new ArrayList<Movimentacao>();
		
		try {
			String nomeTabelaMovimentacao = new Movimentacao().getNomeTabela();
			String sql = "SELECT * FROM " + nomeTabelaMovimentacao;
			
			PreparedStatement statement = DatabaseConfig.conexao.prepareStatement(sql);
			ResultSet result = statement.executeQuery();
			
			while(result.next()) {
				Movimentacao movimentacao = new Movimentacao(result.getInt("id"), result.getInt("produto_id"),
						result.getInt("quantidade"), result.getString("local"), result.getString("tipo_local"));
				
				movimentacoes.add(movimentacao);
			}
			
			return movimentacoes;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
