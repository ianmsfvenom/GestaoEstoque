package gestaoestoque.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import gestaoestoque.config.DatabaseConfig;

public class Produto extends BaseModel {
	private int codigo;
	private String nome;
	private String categoria;
	private int fornecedorId;
	private String unidadeMedida;
	private double precoCompra;
	private double precoVenda;
	private String descricao;
	private String codigoBarra;
	private Fornecedor fornecedor;
	
	public Produto() {
		
	}
	
	public Produto(int codigo, String nome, String categoria, int fornecedorId, String unidadeMedida,
			double precoCompra, double precoVenda, String descricao, String codigoBarra) {
		this.codigo = codigo;
		this.nome = nome;
		this.categoria = categoria;
		this.fornecedorId = fornecedorId;
		this.unidadeMedida = unidadeMedida;
		this.precoCompra = precoCompra;
		this.precoVenda = precoVenda;
		this.descricao = descricao;
		this.codigoBarra = codigoBarra;
	}

	@Override
	public String getNomeTabela() {
		return "produtos";
	}
	
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public int getFornecedorId() {
		return fornecedorId;
	}
	public void setFornecedorId(int fornecedorId) {
		this.fornecedorId = fornecedorId;
	}
	public String getUnidadeMedida() {
		return unidadeMedida;
	}
	public void setUnidadeMedida(String unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}
	public double getPrecoCompra() {
		return precoCompra;
	}
	public void setPrecoCompra(double precoCompra) {
		this.precoCompra = precoCompra;
	}
	public double getPrecoVenda() {
		return precoVenda;
	}
	public void setPrecoVenda(double precoVenda) {
		this.precoVenda = precoVenda;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getCodigoBarra() {
		return codigoBarra;
	}
	public void setCodigoBarra(String codigoBarra) {
		this.codigoBarra = codigoBarra;
	}
	
	public Fornecedor getFornecedor() {
		try {
			String nomeTabelaFornecedor = new Fornecedor().getNomeTabela();
			String sql = "SELECT * FROM " + nomeTabelaFornecedor + " WHERE id = ?";
			PreparedStatement statement = DatabaseConfig.conexao.prepareStatement(sql);
			statement.setInt(1, fornecedorId);
			
			ResultSet result = statement.executeQuery();
			
			while(result.next()) {
				int id = result.getInt("id");
				String nome = result.getString("nome");
				String telefone = result.getString("telefone");
				String condicaoPagamento = result.getString("condicao_pagamento");
				fornecedor = new Fornecedor(id, nome, telefone, condicaoPagamento);
			}
			
			return fornecedor;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	@Override
	public void deletar() throws Exception {
		String sql = "DELETE FROM " + getNomeTabela() + " WHERE codigo = ?";
		PreparedStatement statement = DatabaseConfig.conexao.prepareStatement(sql);
		statement.setInt(1, codigo);
        int linhasAfetadas = statement.executeUpdate();
        
        if (linhasAfetadas > 0) {
            System.out.println("Registro deletado com sucesso!");
        } else {
            System.out.println("Nenhum registro encontrado com esse ID.");
        }
	}
	
	@Override
	public void inserir() throws Exception {
		String sql = "INSERT INTO " + getNomeTabela() 
				+ "(codigo, nome, categoria, fornecedor_id, unidade_medida, preco_compra, preco_venda, descricao, codigo_barra)"
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement statement = DatabaseConfig.conexao.prepareStatement(sql);
		
		statement.setInt(1, codigo);
		statement.setString(2, nome);
		statement.setString(3, categoria);
		statement.setInt(4, fornecedorId);
		statement.setString(5, unidadeMedida);
		statement.setDouble(6, precoCompra);
		statement.setDouble(7, precoVenda);
		statement.setString(8, descricao);
		statement.setString(9, codigoBarra);
		
        int adicionado = statement.executeUpdate();
        
        if (adicionado > 0) {
            System.out.println("Registro inserido com sucesso!");
        } else {
        	System.out.println("Falha ao inserir registro");
        }
	}
	
	
	public static ArrayList<Produto> carregarTodosProdutos() {
		ArrayList<Produto> produtos = new ArrayList<Produto>();
		
		try {
			String nomeTabelaProdutos = new Produto().getNomeTabela();
			String sql = "SELECT * FROM " + nomeTabelaProdutos;
			
			PreparedStatement statement = DatabaseConfig.conexao.prepareStatement(sql);
			ResultSet result = statement.executeQuery();
			
			while(result.next()) {
				Produto produto = new Produto(result.getInt("codigo"), 
						result.getString("nome"),
						result.getString("categoria"),
						result.getInt("fornecedor_id"),
						result.getString("unidade_medida"),
						result.getDouble("preco_compra"),
						result.getDouble("preco_venda"),
						result.getString("descricao"),
						result.getString("codigo_barra"));
				
				produtos.add(produto);
			}
			
			return produtos;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Produto carregarPeloCodigo(int codigo) throws Exception {
		String sql = "SELECT * FROM " + new Produto().getNomeTabela() + " WHERE codigo = ?";
		PreparedStatement stmt = DatabaseConfig.conexao.prepareStatement(sql);
		
		stmt.setInt(1, codigo);
		
		ResultSet result = stmt.executeQuery();
		Produto produto = null;
		while(result.next()) {
			int cod = result.getInt("codigo");
			String nome = result.getString("nome");
			String categoria = result.getString("categoria");
			int fornecedorId = result.getInt("fornecedor_id");
			String unidadeMedida = result.getString("unidade_medida");
			double precoCompra = result.getDouble("preco_compra");
			double precoVenda = result.getDouble("preco_venda");
			String descricao = result.getString("descricao");
			String codigoBarra = result.getString("codigo_barra");
			produto = new Produto(cod, nome, categoria, fornecedorId, unidadeMedida, precoCompra, precoVenda, descricao, codigoBarra);
		}
		
		return produto;
	}
	
	@Override
	public boolean salvar() throws Exception {
		String sql = "UPDATE " + getNomeTabela() + " SET nome = ?, categoria = ?, fornecedor_id = ?, unidade_medida = ?, "
				+ "preco_compra = ?, preco_venda = ?, descricao = ?, codigo_barra = ? WHERE codigo = ?";
		
		PreparedStatement stmt = DatabaseConfig.conexao.prepareStatement(sql);
		stmt.setString(1, nome);
		stmt.setString(2, categoria);
		stmt.setInt(3, fornecedorId);
		stmt.setString(4, unidadeMedida);
		stmt.setDouble(5, precoCompra);
		stmt.setDouble(6, precoVenda);
		stmt.setString(7, descricao);
		stmt.setString(8, codigoBarra);
		stmt.setInt(9, codigo);
		
		return stmt.executeUpdate() > 0;
	}
	
	@Override
	public String toString() {
		return nome;
	}
}
