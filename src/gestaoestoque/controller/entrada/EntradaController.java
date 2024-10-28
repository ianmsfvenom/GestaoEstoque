package gestaoestoque.controller.entrada;

import java.net.URL;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.ResourceBundle;

import gestaoestoque.models.Entrada;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class EntradaController implements Initializable {

    @FXML
    private Button cadastrarEntradaButton;

    @FXML
    private TableColumn<Entrada, String> entradaColuna;

    @FXML
    private ComboBox<String> entradaEscolha;

    @FXML
    private AnchorPane entradaPane;

    @FXML
    private Label entradasLabel;

    @FXML
    private TableColumn<Entrada, String>  produtoColuna;

    @FXML
    private ComboBox<Produto> produtoEscolha;

    @FXML
    private TableColumn<Entrada, String>  quantidadeColuna;

    @FXML
    private Spinner<Integer> quantidadeSpinner;
    
    @FXML
    private TableView<Entrada> entradasTable;

    @FXML
    private Label valorTotalCompraLabel;
   
    @FXML
    private Label valorTotalVendaLabel;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	ArrayList<Entrada> entradas = Entrada.carregarTodasEntradas();
    	ArrayList<Produto> produtos = Produto.carregarTodosProdutos();
 
    	produtoEscolha.setItems(FXCollections.observableArrayList(produtos));
    	
    	entradaEscolha.setItems(FXCollections.observableArrayList("Compra de fornecedor", "Devolução de cliente", "Produção interna"));
    	
    	produtoColuna.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getProduto().getNome()));
    	quantidadeColuna.setCellValueFactory(new PropertyValueFactory<Entrada, String>("quantidade"));
    	entradaColuna.setCellValueFactory(new PropertyValueFactory<Entrada, String>("entrada"));
    	entradasTable.setItems(FXCollections.observableArrayList(entradas));
    	
    	quantidadeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));
    	
    	entradasTable.setRowFactory(param -> {
    		TableRow<Entrada> row = new TableRow<Entrada>();
    		row.setOnMouseClicked(event -> {
    			if (event.getClickCount() == 2 && !row.isEmpty()) {
    				showDoubleClickDialog(row.getItem(), row.getIndex());
                }
    		});
    		
    		return row;
    	});
    	
    	entradasLabel.setText(Integer.toString(entradas.size()));
    	
    	carregarValorTotal();
    }
    
    private void showDoubleClickDialog(Entrada item, int index) {
    	Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Ação");
        dialog.setHeaderText("Deseja deletar a entrada para " + item.getProduto().getNome() + "?");

        ButtonType delete = new ButtonType("Deletar entrada");
        ButtonType cancelar =  new ButtonType("Cancelar");
        
        dialog.getButtonTypes().setAll(delete, cancelar);
        
        dialog.showAndWait().ifPresent(response -> {
        	if(response == delete) {
        		try {
					item.deletar();
					entradasTable.getItems().remove(item);
					entradasLabel.setText(Integer.toString(entradasTable.getItems().size()));
					DialogUtils.mostrarDialogoSucesso("Sucesso!", "Entrada deletada com sucesso!");
					carregarValorTotal();
				} catch (Exception e) {
					e.printStackTrace();
					DialogUtils.mostrarDialogoErro("Erro!", "Falha ao deletar entrada");
				}
        	} else {
        		dialog.close();
        	}
        });
	}

	@FXML
    public void onClickEntradaButton(ActionEvent event) {
    	if(produtoEscolha.getValue() == null || entradaEscolha.getValue() == null || quantidadeSpinner.getValue() == 0) {
    		DialogUtils.mostrarDialogoErro("Erro!", "Preencha todos os campos!");
    		return;
    	}
    	
    	try {
        	Entrada entrada = new Entrada(0, produtoEscolha.getValue().getCodigo(), entradaEscolha.getValue(), quantidadeSpinner.getValue());
			entrada.inserir();			
			entradasTable.getItems().add(entrada);
			entradasLabel.setText(Integer.toString(entradasTable.getItems().size()));
	    	DialogUtils.mostrarDialogoSucesso("Sucesso!", "Entrada cadastrada com sucesso!");
			carregarValorTotal();

		} catch (Exception e) {
			DialogUtils.mostrarDialogoErro("Erro!", e.getMessage());
		}
    }
	
	void carregarValorTotal() {
    	ArrayList<Entrada> entradas = Entrada.carregarTodasEntradas();

    	double valorTotalVenda = 0;
    	double valorTotalCompra = 0;
    	for(Entrada entrada: entradas) {
    		double vendas = entrada.getQuantidade() * entrada.getProduto().getPrecoVenda();
    		double compras = entrada.getQuantidade() * entrada.getProduto().getPrecoCompra();
    		
    		valorTotalVenda += vendas;
    		valorTotalCompra += compras;
    	}
    	
    	valorTotalCompraLabel.setText(Double.toString(valorTotalCompra));
    	valorTotalVendaLabel.setText(Double.toString(valorTotalVenda));

	}

}
