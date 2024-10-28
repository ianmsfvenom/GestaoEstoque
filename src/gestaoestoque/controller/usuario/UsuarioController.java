package gestaoestoque.controller.usuario;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import gestaoestoque.Main;
import gestaoestoque.controller.estoque.EditarEstoqueController;
import gestaoestoque.models.Usuario;
import gestaoestoque.utils.DialogUtils;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UsuarioController implements Initializable {
	
	@FXML
	TextField usuarioEntrada;
	@FXML
	ChoiceBox<String> tipoUsuarioEscolha;
	@FXML
	PasswordField senhaEntrada;
	@FXML
	PasswordField confirmarSenhaEntrada;
	@FXML
	Button criarUsuarioButton;
	@FXML
	TableView<Usuario> usuarioTable;
	@FXML
	TableColumn<Usuario, String> usuarioColuna;
	@FXML
	TableColumn<Usuario, String> tipoUsuarioColuna;
	@FXML
	HBox box1;
	@FXML
	VBox box2;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ArrayList<Usuario> usuarios = Usuario.carregarTodosUsuarios();
		tipoUsuarioEscolha.setItems(FXCollections.observableArrayList("Administrador", "Comum"));
		usuarioColuna.setCellValueFactory(new PropertyValueFactory<Usuario, String>("nome"));
		tipoUsuarioColuna.setCellValueFactory(new PropertyValueFactory<Usuario, String>("papel"));
		usuarioTable.setItems(FXCollections.observableArrayList(usuarios));
		usuarioTable.setRowFactory(param -> {
			TableRow<Usuario> row = new TableRow<Usuario>();
			row.setOnMouseClicked(event -> {
				if(event.getClickCount() == 2 && row != null)
					mostrarPainelEdicao(row.getItem(), row.getIndex());
			});
			return row;
		});
		
		if(Main.usuarioAutenticado.getPapel().equals("Comum")) {
			box1.setVisible(false);
			box2.setVisible(false);
			criarUsuarioButton.setVisible(false);
		}
	}
	
	@FXML
	public void onClickUsuarioButton(ActionEvent event) {
		if(senhaEntrada.getText().isEmpty() || confirmarSenhaEntrada.getText().isEmpty() 
				|| usuarioEntrada.getText().isEmpty() || tipoUsuarioEscolha.getValue().isEmpty() || tipoUsuarioEscolha.getValue() == null) {
			DialogUtils.mostrarDialogoErro("Erro!", "Preencha todos os campos!");
			return;
		}
		if(!senhaEntrada.getText().equals(confirmarSenhaEntrada.getText())) {
			DialogUtils.mostrarDialogoErro("Erro!", "As senhas devem ser iguais!");
			return;
		}
		
		Usuario usuario = new Usuario(0, tipoUsuarioEscolha.getValue(), usuarioEntrada.getText(), senhaEntrada.getText());
		try {
			usuario.inserir();
			ArrayList<Usuario> usuarios = Usuario.carregarTodosUsuarios();
			usuarioTable.setItems(FXCollections.observableArrayList(usuarios));
			DialogUtils.mostrarDialogoSucesso("Sucesso!", "Usuario adicionado com sucesso!");
			
			usuarioEntrada.setText("");
			senhaEntrada.setText("");
			confirmarSenhaEntrada.setText("");
			
		} catch (Exception e) {
			DialogUtils.mostrarDialogoErro("Erro!", e.getMessage());
		}
	}

	public void mostrarPainelEdicao(Usuario usuario, int index) {
		Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Ação");
        dialog.setHeaderText("Escolha uma opção para o usuário " + usuario.getNome());

        ButtonType edit = new ButtonType("Editar usuário");
        ButtonType delete = new ButtonType("Deletar usuário");
        ButtonType cancelar =  new ButtonType("Cancelar");

        dialog.getButtonTypes().setAll(edit, delete, cancelar);

        dialog.showAndWait().ifPresent(response -> { 
        	try {
	        	if(response == edit) {
	        		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestaoestoque/fxmls/usuario/editar-usuario.fxml"));
	               	Parent root = loader.load();
	               	EditarUsuarioController controller = loader.getController();
	               	controller.carregarDados(usuario, index, usuarioTable.getItems());
	               	Scene scene = new Scene(root);
	               	Stage stage = new Stage();
	               	stage.setTitle("Editar estoque");
	               	stage.setScene(scene);
	               	stage.initModality(Modality.APPLICATION_MODAL);
	               	stage.showAndWait();
	               	dialog.close();
	        	} else if(response == delete) {
	        		usuarioTable.getItems().remove(usuario);
	        		usuario.deletar();
	        		dialog.close();
	        	} else {
	        		dialog.close();
	        	}
        	} catch (Exception e) {
				DialogUtils.mostrarDialogoErro("Erro!", e.getMessage());
			}
        });
	}
}
