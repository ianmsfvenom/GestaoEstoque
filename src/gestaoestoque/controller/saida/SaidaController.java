package gestaoestoque.controller.saida;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import gestaoestoque.models.Produto;
import gestaoestoque.models.Saida;
import gestaoestoque.utils.DialogUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class SaidaController implements Initializable {

    @FXML
    private Button cadastrarSaidaButton;

    @FXML
    private TableColumn<Saida, String> produtoColuna;

    @FXML
    private ComboBox<Produto> produtoEscolha;

    @FXML
    private TableColumn<Saida, String> quantidadeColuna;

    @FXML
    private Spinner<Integer> quantidadeSpinner;

    @FXML
    private TableColumn<Saida, String> saidaColuna;

    @FXML
    private TextField saidaEntrada;

    @FXML
    private AnchorPane saidaPane;

    @FXML
    private TableView<Saida> saidasTabela;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	ArrayList<Produto> produtos = Produto.carregarTodosProdutos();
    	ArrayList<Saida> saidas = Saida.carregarTodasSaidas();
    	
    	produtoEscolha.setItems(FXCollections.observableArrayList(produtos));
    	
    	produtoColuna.setCellValueFactory(param -> 
    		new SimpleStringProperty(param.getValue().getProduto().getNome())
    	);
    	
    	quantidadeColuna.setCellValueFactory(new PropertyValueFactory<Saida, String>("quantidade"));
    	saidaColuna.setCellValueFactory(new PropertyValueFactory<Saida, String>("saida"));
    	quantidadeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));
    	
    	saidasTabela.setItems(FXCollections.observableArrayList(saidas));
    	
    	saidasTabela.setRowFactory(param -> {
    		TableRow<Saida> row = new TableRow<Saida>();
    		row.setOnMouseClicked(event -> {
    			if(event.getClickCount() == 2 && row != null) 
    				showDeleteDialog(row.getItem());
    		});
    		return row;
    	});
    }

    @FXML
    void onClickSaidaButton(ActionEvent event) {
    	if(saidaEntrada.getText().isEmpty() || quantidadeSpinner.getValue() <= 0 || produtoEscolha.getValue() == null) {
    		DialogUtils.mostrarDialogoErro("Erro!", "Preencha todos os campos");
    		return;
    	}
    	
    	Saida saida = new Saida(0, produtoEscolha.getValue().getCodigo(), saidaEntrada.getText(), quantidadeSpinner.getValue());
    	try {
			saida.inserir();
			saidasTabela.getItems().add(saida);
	    	DialogUtils.mostrarDialogoSucesso("Sucesso!", "Saida cadastrada com sucesso!");
		} catch (Exception e) {
			DialogUtils.mostrarDialogoErro("Erro!", e.getMessage());
		}
    }
    
    public void showDeleteDialog(Saida item) {
    	Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Ação");
        dialog.setHeaderText("Deseja deletar a saida de " + item.getProduto().getNome() + "?");

        ButtonType delete = new ButtonType("Deletar saída");
        ButtonType cancelar =  new ButtonType("Cancelar");
        
        dialog.getButtonTypes().setAll(delete, cancelar);
        
        dialog.showAndWait().ifPresent(response -> {
        	if(response == delete) {
        		try {
					item.deletar();
					saidasTabela.getItems().remove(item);
					DialogUtils.mostrarDialogoSucesso("Sucesso!", "Saída deletada com sucesso!");
			    	ArrayList<Saida> saidas = Saida.carregarTodasSaidas();
			    	saidasTabela.setItems(FXCollections.observableArrayList(saidas));
        		} catch (Exception e) {
					e.printStackTrace();
					DialogUtils.mostrarDialogoErro("Erro!", "Falha ao deletar saída");
				}
        	} else {
        		dialog.close();
        	}
        });
    }

}
