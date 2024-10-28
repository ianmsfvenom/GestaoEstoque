package gestaoestoque;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.prefs.Preferences;

import gestaoestoque.config.DatabaseConfig;
import gestaoestoque.models.Usuario;
import gestaoestoque.utils.DialogUtils;
import gestaoestoque.utils.PreferencesUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	public static Usuario usuarioAutenticado = null;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			DatabaseConfig.conexao = DriverManager.getConnection(DatabaseConfig.DB_URL, DatabaseConfig.DB_USER, DatabaseConfig.DB_PASS);
			if(DatabaseConfig.conexao == null) 
				throw new Exception("Erro ao estabelecer conexao");
			System.out.println("Conexao ao banco de dados estabelecida!");
			
			if(!PreferencesUtils.isAutenticado()) {
				Parent rootLogin = FXMLLoader.load(getClass().getResource("fxmls/login.fxml"));
		        primaryStage.setTitle("Fazer Login");
		        primaryStage.setScene(new Scene(rootLogin));
		        primaryStage.show();
		        primaryStage.setResizable(false);
		        return;
			}
			
			PreferencesUtils.carregarUsuarioAutenticado();
			
			Parent rootPainel = FXMLLoader.load(getClass().getResource("fxmls/Painel.fxml"));
	        primaryStage.setTitle("Gestão de Estoque");
	        primaryStage.setScene(new Scene(rootPainel));
	        primaryStage.show();
	        primaryStage.setResizable(false);
		} catch(SQLException e) {
			DialogUtils.mostrarDialogoErro("Erro!", "Erro ao estabelecer conexao com o banco de dados");
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
