package gestaoestoque.config;

import java.sql.Connection;

public class DatabaseConfig {
	public static String DB_URL = "jdbc:mysql://localhost:3306/gestao_estoque";
	public static String DB_USER = "root";
	public static String DB_PASS = "";
	
	public static Connection conexao;
}
