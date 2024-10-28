package gestaoestoque.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import gestaoestoque.config.DatabaseConfig;

public class Fornecedor extends BaseModel {
	private int id;
	private String nome;
	private String telefone;
	private String condicaoPagamento;
	
	@Override
	public String getNomeTabela() {
		return "fornecedores";
	}

	public Fornecedor() {
		
	}
	
	public Fornecedor(int id, String nome, String telefone, String condicaoPagamento) {
		this.id = id;
		this.nome = nome;
		this.condicaoPagamento = condicaoPagamento;
		this.telefone = telefone;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCondicaoPagamento() {
		return condicaoPagamento;
	}
	public void setCondicaoPagamento(String condicaoPagamento) {
		this.condicaoPagamento = condicaoPagamento;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
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
	
	@Override
	public void inserir() throws Exception {
		String sql = "INSERT INTO " + getNomeTabela() + "(nome, telefone, condicao_pagamento) VALUES (?, ?, ?)";
		PreparedStatement statement = DatabaseConfig.conexao.prepareStatement(sql);
		statement.setString(1, nome);
		statement.setString(2, telefone);
		statement.setString(3, condicaoPagamento);
		
        boolean isAdicionado = statement.execute();
        
        if (isAdicionado) {
            System.out.println("Registro inserido com sucesso!");
        } else {
        	System.out.println("Falha ao inserir registro");
        }
	}
	
	public static ArrayList<Fornecedor> carregarTodosFornecedores() {
		ArrayList<Fornecedor> fornecedores = new ArrayList<Fornecedor>();
		
		try {
			String nomeTabela = new Fornecedor().getNomeTabela();
			String sql = "SELECT * FROM " + nomeTabela;
			
			PreparedStatement statement = DatabaseConfig.conexao.prepareStatement(sql);
			ResultSet result = statement.executeQuery();
			
			while(result.next()) {
				Fornecedor fornecedor = new Fornecedor(result.getInt("id"),
						result.getString("nome"),
						result.getString("telefone"),
						result.getString("condicao_pagamento"));
				
				fornecedores.add(fornecedor);
			}
			
			return fornecedores;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public String toString() {
		return nome;
	}
}
