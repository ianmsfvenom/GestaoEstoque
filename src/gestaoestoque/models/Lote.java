package gestaoestoque.models;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import gestaoestoque.config.DatabaseConfig;

public class Lote extends BaseModel {
	private Produto produto;
	private int id;
	private int produtoId;
	private int quantidade;
	private String nome;
	private Date dataValidade;
	
	@Override
	public String getNomeTabela() {
		return "lotes";
	}
	
	public Lote() {}
	public Lote(int id, int produtoId, int quantidade, String nome, Date dataValidade) {
		this.id = id;
		this.produtoId = produtoId;
		this.quantidade = quantidade;
		this.nome = nome;
		this.dataValidade = dataValidade;
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
		+ "(nome, data_validade, produto_id, quantidade)"
		+ "VALUES (?, ?, ?, ?)";
		PreparedStatement statement = DatabaseConfig.conexao.prepareStatement(sql);
		
		
		statement.setString(1, nome);
		statement.setDate(2, dataValidade);
		statement.setInt(3, produtoId);
		statement.setInt(4, quantidade);
		
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
	
	public static ArrayList<Lote> carregarTodosLotes() {
		ArrayList<Lote> lotes = new ArrayList<Lote>();
		try {
			String sql = "SELECT * FROM " + new Lote().getNomeTabela();
			PreparedStatement stmt = DatabaseConfig.conexao.prepareStatement(sql);
			ResultSet result = stmt.executeQuery();
			
			while(result.next()) {
				Lote lote = new Lote(result.getInt("id"), result.getInt("produto_id"), result.getInt("quantidade"),
						result.getString("nome"), result.getDate("data_validade"));
				lotes.add(lote);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return lotes;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDataValidade() {
		return dataValidade;
	}

	public void setDataValidade(Date dataValidade) {
		this.dataValidade = dataValidade;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	
	
}
