package gestaoestoque.controller.fornecedor;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import gestaoestoque.Main;
import gestaoestoque.models.Fornecedor;
import gestaoestoque.utils.DialogUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class FornecedorController implements Initializable {
	// Painel de fornecedores
    @FXML
    private TableView<Fornecedor> fornecedoresTable;
    @FXML
    private TableColumn<Fornecedor, String> nomeFornecedoresColuna;
    @FXML
    private TableColumn<Fornecedor, String> telFornecedoresColuna;
    @FXML
    private TableColumn<Fornecedor, String> cpFornecedoresColuna;
    @FXML
    private ChoiceBox<String> condicaoPagamentoFornecedor;
    @FXML
    private TextField nomeFornecedorField;
    @FXML
    private TextField telFornecedorField;
    @FXML
    private Button addFornecedorButton;
    @FXML
    private HBox box1;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	ArrayList<Fornecedor> fornecedores = Fornecedor.carregarTodosFornecedores();
    	ObservableList<Fornecedor> observableList = FXCollections.observableArrayList(fornecedores);
    	nomeFornecedoresColuna.setCellValueFactory(new PropertyValueFactory<Fornecedor, String>("nome"));
    	telFornecedoresColuna.setCellValueFactory(new PropertyValueFactory<Fornecedor, String>("telefone"));
    	cpFornecedoresColuna.setCellValueFactory(new PropertyValueFactory<Fornecedor, String>("condicaoPagamento"));

    	fornecedoresTable.setItems(observableList);    	
    	condicaoPagamentoFornecedor.setValue("A vista");
    	condicaoPagamentoFornecedor.setItems(FXCollections.observableArrayList("A vista", "Parcelado - sem juros", "Parcelado - com juros"));
    	
    	addFornecedorButton.setOnAction(event -> {
    		if(nomeFornecedorField.getText().isEmpty() || telFornecedorField.getText().isEmpty()) {
    			DialogUtils.mostrarDialogoErro("Erro", "Preencha todos os campos!");
    			return;
    		}
    		
    		Fornecedor fornecedor = new Fornecedor(0, nomeFornecedorField.getText(), 
    				telFornecedorField.getText(), condicaoPagamentoFornecedor.getValue());
    		try {
				fornecedor.inserir();
	    		observableList.add(fornecedor);
	    		nomeFornecedorField.setText("");
	    		telFornecedorField.setText("");

			} catch (Exception e) {
				DialogUtils.mostrarDialogoErro("Erro!", e.getMessage());
			}
    	});
    	
    	if(!Main.usuarioAutenticado.getPapel().equals("Administrador")) {
    		box1.setVisible(false);
    		addFornecedorButton.setVisible(false);
    		return;
    	}
    	fornecedoresTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && fornecedoresTable.getSelectionModel().getSelectedItem() != null) {
                Fornecedor fornecedorSelecionado = fornecedoresTable.getSelectionModel().getSelectedItem();
            	boolean confirmado = DialogUtils.mostrarDialogoConfirmacao(
            			"Confirmar Exclusão", 
            			"Tem certeza de que deseja excluir o contato " + fornecedorSelecionado.getNome() + "?");
            	
            	if(confirmado) {
            		try {
						fornecedorSelecionado.deletar();
	            		observableList.remove(fornecedorSelecionado);
					} catch (Exception e) {
						DialogUtils.mostrarDialogoErro("Erro!", e.getMessage());
					}
            	}
            }
        });
    }
}
