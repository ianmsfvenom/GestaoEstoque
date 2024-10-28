package gestaoestoque.controller.login;

import java.io.IOException;
import java.util.prefs.Preferences;

import gestaoestoque.Main;
import gestaoestoque.models.Usuario;
import gestaoestoque.utils.DialogUtils;
import gestaoestoque.utils.PreferencesUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
	@FXML
	private TextField usuarioEntrada;
	@FXML
	private PasswordField senhaEntrada;	
	@FXML
	private Button loginButton;
	@FXML
	private Button leaveButton;
	
	@FXML
	void onClickLoginButton(ActionEvent event) {
		try {
			if(usuarioEntrada.getText().isEmpty() || senhaEntrada.getText().isEmpty()) {
				DialogUtils.mostrarDialogoErro("Erro!", "Preencha todos os campos!");
				return;
			}
			
			Usuario usuario = Usuario.consultarPeloLoginSenha(usuarioEntrada.getText(), senhaEntrada.getText());
			if(usuario == null) {
				DialogUtils.mostrarDialogoErro("Erro!", "Usuário ou senha inválidos!");
				return;
			}
			DialogUtils.mostrarDialogoSucesso("Sucesso!", "Logado com sucesso!");
			
			Main.usuarioAutenticado = usuario;
			PreferencesUtils.salvarAuth(usuarioEntrada.getText(), senhaEntrada.getText());
			
			Parent root = FXMLLoader.load(getClass().getResource("/gestaoestoque/fxmls/Painel.fxml"));
			Stage stage = new Stage();
			stage.setTitle("Gestão de Estoque");
			stage.setScene(new Scene(root));
			stage.show();
			stage.setResizable(false);
			
			Stage stageAtual = (Stage) loginButton.getScene().getWindow();
			stageAtual.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	void onClickLeaveButton(ActionEvent event) {
		Stage stageAtual = (Stage) leaveButton.getScene().getWindow();
		stageAtual.close();
	}
}
