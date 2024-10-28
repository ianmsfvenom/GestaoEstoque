package gestaoestoque.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import gestaoestoque.config.DatabaseConfig;
import gestaoestoque.utils.CriptoUtils;

public class Usuario extends BaseModel {
	private int id;
	private String papel;
	private String nome;
	private String senha;
	
	@Override
	public String getNomeTabela() {
		return "usuarios";
	}
	
	public Usuario() {}
	public Usuario(int id, String papel, String nome, String senha) {
		this.id = id;
		this.papel = papel;
		this.nome = nome;
		this.senha = senha;
	}
	
	public String getPapel() {
		return papel;
	}
	public void setPapel(String papel) {
		this.papel = papel;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = CriptoUtils.gerarHashComSalt(senha);
	}
	
	@Override
	public void inserir() throws Exception {
		String sql = "INSERT INTO usuarios(nome, papel, senha) VALUES (?, ?, ?)";
		PreparedStatement stmt = DatabaseConfig.conexao.prepareStatement(sql);
		stmt.setString(1, nome);
		stmt.setString(2, papel);
		stmt.setString(3, CriptoUtils.gerarHashComSalt(senha));
		
		int result = stmt.executeUpdate();
		
		if(result <= 0)
			throw new Exception("Falha ao adicionar usuario");
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
	public boolean salvar() throws Exception {
		String sql = "UPDATE " + getNomeTabela() + " SET nome = ?, papel = ?, senha = ? WHERE id = ?";
		PreparedStatement stmt = DatabaseConfig.conexao.prepareStatement(sql);
		stmt.setString(1, nome);
		stmt.setString(2, papel);
		stmt.setString(3, senha);
		stmt.setInt(4, id);
		
		return stmt.executeUpdate() > 0;
	}
	
	public static ArrayList<Usuario> carregarTodosUsuarios() {
		ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
		try {
			String sql = "SELECT * FROM " + new Usuario().getNomeTabela();
			PreparedStatement stmt = DatabaseConfig.conexao.prepareStatement(sql);
			ResultSet result = stmt.executeQuery();
			while(result.next()) 
				usuarios.add(new Usuario(result.getInt("id"), result.getString("papel"), result.getString("nome"), result.getString("senha")));
			return usuarios;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return usuarios;
	}
	
	public static Usuario consultarPeloLoginSenha(String nome, String senha) {
		try {
			String sql = "SELECT * FROM " + new Usuario().getNomeTabela() + " WHERE nome = ?";
			PreparedStatement stmt =  DatabaseConfig.conexao.prepareStatement(sql);
			stmt.setString(1, nome);
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				String senhaCriptografada = result.getString("senha");
				if(CriptoUtils.compararSenha(senha, senhaCriptografada)) 
					return new Usuario(result.getInt("id"), result.getString("papel"), result.getString("nome"), result.getString("senha"));
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		} 
		return null;
	}
}
