package gestaoestoque.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import gestaoestoque.config.DatabaseConfig;

public class Saida extends BaseModel {
	private int id;
	private int produtoId;
	private String saida;
	private int quantidade;
	private Produto produto;
	
	@Override
	public String getNomeTabela() {
		return "saidas";
	}
	
	public Saida() {
		// TODO Auto-generated constructor stub
	}
	
	public Saida(int id, int produtoId, String saida, int quantidade) {
		this.id = id;
		this.produtoId = produtoId;
		this.saida = saida;
		this.quantidade = quantidade;
	}
	
	public int getProdutoId() {
		return produtoId;
	}
	public void setProdutoId(int produtoId) {
		this.produtoId = produtoId;
	}
	public String getSaida() {
		return saida;
	}
	public void setSaida(String saida) {
		this.saida = saida;
	}
	public int getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
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
	public void inserir() throws Exception {
		String sql = "INSERT INTO " + getNomeTabela() 
				+ "(produto_id, saida, quantidade)"
				+ "VALUES (?, ?, ?)";
		PreparedStatement statement = DatabaseConfig.conexao.prepareStatement(sql);
		
		statement.setInt(1, produtoId);
		statement.setString(2, saida);
		statement.setInt(3, quantidade);
		
        int adicionado = statement.executeUpdate();
        
        if (adicionado > 0) {
            System.out.println("Registro inserido com sucesso!");
        } else {
        	System.out.println("Falha ao inserir registro");
        }
	}
	
	public static ArrayList<Saida> procurarMuitosPorSaida(String valor) throws Exception {
		ArrayList<Saida> saidas = new ArrayList<Saida>();
		
		String sql = "SELECT * FROM " + new Saida().getNomeTabela() + " WHERE saida = ?";
		PreparedStatement stmt = DatabaseConfig.conexao.prepareStatement(sql);
		stmt.setString(1, valor);
		ResultSet result = stmt.executeQuery();
		while(result.next()) 
			saidas.add(new Saida(result.getInt("id"), result.getInt("produto_id"), result.getString("saida"), result.getInt("quantidade")));
		
		return saidas;
	}
	
	public static ArrayList<Saida> carregarTodasSaidas() {
		ArrayList<Saida> saidas = new ArrayList<Saida>();
		
		try {
			String nomeTabelaSaida = new Saida().getNomeTabela();
			String sql = "SELECT * FROM " + nomeTabelaSaida;
			
			PreparedStatement statement = DatabaseConfig.conexao.prepareStatement(sql);
			ResultSet result = statement.executeQuery();
			
			while(result.next()) {
				Saida saida = new Saida(result.getInt("id"), result.getInt("produto_id"), result.getString("saida"), result.getInt("quantidade"));
				
				saidas.add(saida);
			}
			
			return saidas;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
