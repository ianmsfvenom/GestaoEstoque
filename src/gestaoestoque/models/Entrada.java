package gestaoestoque.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import gestaoestoque.config.DatabaseConfig;
import gestaoestoque.utils.DialogUtils;

public class Entrada extends BaseModel {
	private int id;
	private String entrada;
	private int produtoId;
	private int quantidade;
	private Produto produto;
	
	@Override
	public String getNomeTabela() {
		return "entradas";
	}
	
	public Entrada() {}
	
	public Entrada(int id, int produtoId, String entrada, int quantidade) {
		super();
		this.id = id;
		this.entrada = entrada;
		this.produtoId = produtoId;
		this.quantidade = quantidade;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEntrada() {
		return entrada;
	}
	public void setEntrada(String entrada) {
		this.entrada = entrada;
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
	public void deletar() throws Exception {
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
		String sql2 = "SELECT * FROM " + new Estoque().getNomeTabela() + " WHERE produto_id = ?";
		PreparedStatement stmt = DatabaseConfig.conexao.prepareStatement(sql2);
		stmt.setInt(1, produtoId);
		
		ResultSet result = stmt.executeQuery();
		
		int sizeResult = 0;
	    while (result.next()) sizeResult++;
	    
		if(sizeResult <= 0) throw new Exception("Estoque nao encontrado");
		
		
		
		String sql = "INSERT INTO " + getNomeTabela() 
		+ "(produto_id, entrada, quantidade)"
		+ "VALUES (?, ?, ?)";
		PreparedStatement statement = DatabaseConfig.conexao.prepareStatement(sql);
		
		statement.setInt(1, produtoId);
		statement.setString(2, entrada);
		statement.setInt(3, quantidade);
		
		int adicionado = statement.executeUpdate();
		
		if (adicionado > 0) {
		    System.out.println("Registro inserido com sucesso!");
		} else {
			System.out.println("Falha ao inserir registro");
		}
	}
	
	public static ArrayList<Entrada> carregarTodasEntradas() {
		ArrayList<Entrada> entradas = new ArrayList<Entrada>();
		
		try {
			String nomeTabelaSaida = new Entrada().getNomeTabela();
			String sql = "SELECT * FROM " + nomeTabelaSaida;
			
			PreparedStatement statement = DatabaseConfig.conexao.prepareStatement(sql);
			ResultSet result = statement.executeQuery();
			
			while(result.next()) {
				Entrada entrada = new Entrada(result.getInt("id"), result.getInt("produto_id"), result.getString("entrada"), result.getInt("quantidade"));
				
				entradas.add(entrada);
			}
			
			return entradas;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ArrayList<Entrada> procurarMuitosPorEntrada(String valor) throws Exception {
		ArrayList<Entrada> entradas = new ArrayList<Entrada>();
		
		String sql = "SELECT * FROM " + new Entrada().getNomeTabela() + " WHERE entrada = ?";
		PreparedStatement stmt = DatabaseConfig.conexao.prepareStatement(sql);
		stmt.setString(1, valor);
		ResultSet result = stmt.executeQuery();
		while(result.next()) 
			entradas.add(new Entrada(result.getInt("id"), result.getInt("produto_id"), result.getString("entrada"), result.getInt("quantidade")));
		
		return entradas;
	}
}
