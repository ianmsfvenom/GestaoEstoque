package gestaoestoque.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import gestaoestoque.config.DatabaseConfig;

public class Armazenagem extends BaseModel {
	private int id;
	private String nome;
	private int produtoId;
	private int quantidade;
	private double valor;
	private Produto produto;
	
	@Override
	public String getNomeTabela() {
		return "armazenagens";
	}
	
	public Armazenagem() {}
	public Armazenagem(int id, int produtoId, int quantidade, double valor, String nome) {
		super();
		this.id = id;
		this.produtoId = produtoId;
		this.quantidade = quantidade;
		this.valor = valor;
		this.nome = nome;
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
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
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
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public void inserir() throws Exception {
		String sql = "INSERT INTO " + getNomeTabela() 
		+ "(produto_id, quantidade, valor, nome)"
		+ "VALUES (?, ?, ?, ?)";
		PreparedStatement statement = DatabaseConfig.conexao.prepareStatement(sql);
		
		statement.setInt(1, produtoId);
		statement.setInt(2, quantidade);
		statement.setDouble(3, valor);
		statement.setString(4, nome);
		
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
	
	public static ArrayList<Armazenagem> carregarTodasArmazenagens() {
		ArrayList<Armazenagem> armazenagens = new ArrayList<Armazenagem>();
		
		try {
			String nomeTabelaArmazenagem = new Armazenagem().getNomeTabela();
			String sql = "SELECT * FROM " + nomeTabelaArmazenagem;
			
			PreparedStatement statement = DatabaseConfig.conexao.prepareStatement(sql);
			ResultSet result = statement.executeQuery();
			
			while(result.next()) {
				Armazenagem armazenagem = new Armazenagem(result.getInt("id"), result.getInt("produto_id"), 
						result.getInt("quantidade"), result.getDouble("valor"), result.getString("nome"));
				armazenagens.add(armazenagem);
			}
			
			return armazenagens;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
