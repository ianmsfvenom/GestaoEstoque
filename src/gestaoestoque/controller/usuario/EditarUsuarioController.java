package gestaoestoque.controller.usuario;

import java.net.URL;
import java.util.ResourceBundle;

import gestaoestoque.models.Usuario;
import gestaoestoque.utils.DialogUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class EditarUsuarioController implements Initializable{
	
	@FXML
	private TextField usuarioEntrada;
	@FXML
	private PasswordField senhaEntrada;
	@FXML
	private PasswordField confirmarSenhaEntrada;
	@FXML
	private ChoiceBox<String> tipoUsuarioEscolha;
	@FXML
	private Button salvarButton;
	
	private Usuario usuario;
	private int index;
	private ObservableList<Usuario> usuarios;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tipoUsuarioEscolha.setItems(FXCollections.observableArrayList("Administrador", "Comum"));
	}
	
	
	public void carregarDados(Usuario usuario, int index, ObservableList<Usuario> usuarios) {
		this.usuario = usuario;
		this.index = index;
		this.usuarios = usuarios;
		
		usuarioEntrada.setText(usuario.getNome());
		tipoUsuarioEscolha.setValue(usuario.getPapel());
	}
	
	@FXML
	public void onClickSalvarButton(ActionEvent event) {
		if(usuarioEntrada.getText().isEmpty() || tipoUsuarioEscolha.getValue().isEmpty() || tipoUsuarioEscolha.getValue() == null) {
			DialogUtils.mostrarDialogoErro("Erro!", "Preencha os campos obrigatorios!");
			return;
		}
		
		if((!senhaEntrada.getText().isEmpty() || !confirmarSenhaEntrada.getText().isEmpty()) && 
				!senhaEntrada.getText().equals(confirmarSenhaEntrada.getText())) {
			DialogUtils.mostrarDialogoErro("Erro!", "As senhas devem ser iguais!");
			return;
		}
		
		usuario.setNome(usuarioEntrada.getText());
		usuario.setPapel(tipoUsuarioEscolha.getValue());
		
		if(!senhaEntrada.getText().isEmpty())
			usuario.setSenha(senhaEntrada.getText());
		
		try {
			if(usuario.salvar()) {
				DialogUtils.mostrarDialogoSucesso("Sucesso!", "Usuario editado com sucesso!");
				usuarios.set(index, usuario);
			} else { 
				DialogUtils.mostrarDialogoErro("Erro!", "Não foi possivel editar o usuario!");
			}
		} catch (Exception e) {
			DialogUtils.mostrarDialogoErro("Erro!", e.getMessage());
		}
	}
}
