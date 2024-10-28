package gestaoestoque.controller.painel;

import java.awt.Dialog;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import gestaoestoque.Main;
import gestaoestoque.factory.celulas.ProdutoCelulaFactory;
import gestaoestoque.models.Fornecedor;
import gestaoestoque.models.Produto;
import gestaoestoque.utils.DialogUtils;
import gestaoestoque.utils.PreferencesUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class PainelController implements Initializable {
	
	
	@FXML
	private Pane centerPane;
    
    // Botoes da barra lateral
    @FXML
    private Button produtoSideButton;
    @FXML
    private Button saidaSideButton;
    @FXML
    private Button entradaSideButton;
    @FXML
    private Button estoqueSideButton;
    @FXML
    private Button movimentacaoSideButton;
    @FXML
    private Button relatorioSideButton;
    @FXML
    private Button fornecedorSideButton;
    @FXML
    private Button loteSideButton;
    @FXML
    private Button compraVendaSideButton;
    @FXML
    private Button custoSideButton;
    @FXML
    private Button usuarioSideButton;
    @FXML
    private Button logoutButton;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	
		try {
			Parent conteudo = FXMLLoader.load(getClass().getResource("/gestaoestoque/fxmls/fornecedor.fxml"));
			centerPane.getChildren().setAll(conteudo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(!Main.usuarioAutenticado.getPapel().equals("Administrador")) {
			desativarBotaoLateral(usuarioSideButton);
		}
    	
    	produtoSideButton.setOnAction(event -> mudarPainel("produto"));
    	saidaSideButton.setOnAction(event -> mudarPainel("saida"));
    	entradaSideButton.setOnAction(event -> mudarPainel("entrada"));
    	estoqueSideButton.setOnAction(event -> mudarPainel("estoque"));
    	movimentacaoSideButton.setOnAction(event -> mudarPainel("movimentacao"));
    	relatorioSideButton.setOnAction(event -> mudarPainel("relatorio"));
    	fornecedorSideButton.setOnAction(event -> mudarPainel("fornecedor"));
    	loteSideButton.setOnAction(event -> mudarPainel("lote"));
    	compraVendaSideButton.setOnAction(event -> mudarPainel("compra-venda"));
    	custoSideButton.setOnAction(event -> mudarPainel("custo"));
    	usuarioSideButton.setOnAction(event -> mudarPainel("usuario"));
    }
    
    private void mudarPainel(String painel) {
        try {
			Parent conteudo = FXMLLoader.load(getClass().getResource("/gestaoestoque/fxmls/" + painel + ".fxml"));
			centerPane.getChildren().setAll(conteudo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    
    
    @FXML
    void onClickLogoutButton(ActionEvent event) {
    	try {
    		PreferencesUtils.removerAuth();
	    	Parent root = FXMLLoader.load(getClass().getResource("/gestaoestoque/fxmls/login.fxml"));
			Stage stage = new Stage();
			stage.setTitle("Gestão de Estoque");
			stage.setScene(new Scene(root));
			stage.show();
			stage.setResizable(false);
	    	Stage stageAtual = (Stage) logoutButton.getScene().getWindow();
			stageAtual.close();
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    void desativarBotaoLateral(Button botaoLateral) {
    	botaoLateral.setVisible(false);
    	botaoLateral.setManaged(false);
    }
}
