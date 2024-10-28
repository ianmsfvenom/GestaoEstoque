package gestaoestoque.controller.movimentacao;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import gestaoestoque.models.Entrada;
import gestaoestoque.models.Movimentacao;
import gestaoestoque.models.Produto;
import gestaoestoque.utils.DialogUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class MovimentacaoController implements Initializable {

    @FXML
    private TextField armazenamentoField;
    @FXML
    private Button cadastrarMovimentacaoButton;
    @FXML
    private TableColumn<Movimentacao, String> localColuna;
    @FXML
    private ChoiceBox<String> tipoLocalEscolha;
    @FXML
    private AnchorPane movimentacaoPane;
    @FXML
    private TableView<Movimentacao> movimentacaoTable;
    @FXML
    private TableColumn<Movimentacao, String> produtoColuna;
    @FXML
    private ChoiceBox<Produto> produtoEscolha;
    @FXML
    private TableColumn<Movimentacao, String> quantidadeColuna;
    @FXML
    private Spinner<Integer> quantidadeSpinner;
    @FXML
    private TableColumn<Movimentacao, String> tipoLocalColuna;

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	ArrayList<Movimentacao> movimentacoes = Movimentacao.carregarTodasMovimentacoes();
    	
    	ArrayList<Produto> produtos = Produto.carregarTodosProdutos();
    	produtoEscolha.setItems(FXCollections.observableArrayList(produtos));
    	
    	produtoColuna.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getProduto().getNome()));
    	quantidadeColuna.setCellValueFactory(new PropertyValueFactory<Movimentacao, String>("quantidade"));
    	localColuna.setCellValueFactory(new PropertyValueFactory<Movimentacao, String>("local"));
    	tipoLocalColuna.setCellValueFactory(new PropertyValueFactory<Movimentacao, String>("tipoLocal"));

    	tipoLocalEscolha.setItems(FXCollections.observableArrayList("Filial", "Deposito"));
    	quantidadeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));

    	movimentacaoTable.setItems(FXCollections.observableArrayList(movimentacoes));
    	movimentacaoTable.setRowFactory(param -> {
    		TableRow<Movimentacao> row = new TableRow<Movimentacao>();
    		
    		row.setOnMouseClicked( event -> {
    			if(event.getClickCount() == 2 && !row.isEmpty())
    				showDoubleClickDialog(row.getItem(), row.getIndex());
    		});
    		
    		return row;
    	});
    }
    
    @FXML
    public void onClickMovimentacaoButton(ActionEvent event) {
    	if(produtoEscolha.getValue() == null || armazenamentoField.getText().isEmpty() || quantidadeSpinner.getValue() == 0 || tipoLocalEscolha.getValue() == null) {
    		DialogUtils.mostrarDialogoErro("Erro!", "Preencha todos os campos!");
    		return;
    	}
    	
    	Movimentacao movimentacao = new Movimentacao(0, produtoEscolha.getValue().getCodigo(), quantidadeSpinner.getValue(), 
    			armazenamentoField.getText(), tipoLocalEscolha.getValue());
    	
    	try {
			movimentacao.inserir();
			movimentacaoTable.getItems().add(movimentacao);
			DialogUtils.mostrarDialogoSucesso("Sucesso!", "Movimentacao adicionada com sucesso!");
    	} catch (Exception e) {
			DialogUtils.mostrarDialogoErro("Erro!", e.getMessage());
		}
    		
    }
    
    private void showDoubleClickDialog(Movimentacao item, int index) {
    	Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Ação");
        dialog.setHeaderText("Deseja deletar a movimentacao do produto " + item.getProduto().getNome() + " para " + item.getLocal() + "?");

        ButtonType delete = new ButtonType("Deletar movimentacao");
        ButtonType cancelar =  new ButtonType("Cancelar");
        
        dialog.getButtonTypes().setAll(delete, cancelar);
        
        dialog.showAndWait().ifPresent(response -> {
        	if(response == delete) {
        		try {
					item.deletar();
					movimentacaoTable.getItems().remove(item);
					DialogUtils.mostrarDialogoSucesso("Sucesso!", "Movimentação deletada com sucesso!");
				} catch (Exception e) {
					e.printStackTrace();
					DialogUtils.mostrarDialogoErro("Erro!", "Falha ao deletar movimentação");
				}
        	} else {
        		dialog.close();
        	}
        });
	}

}
