package gestaoestoque.controller.estoque;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import gestaoestoque.Main;
import gestaoestoque.models.Estoque;
import gestaoestoque.models.Produto;
import gestaoestoque.utils.DialogUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EstoqueController implements Initializable {

    @FXML
    private Button cadastrarEstoqueButton;

    @FXML
    private AnchorPane estoquePane;

    @FXML
    private Spinner<Integer> maximaSpinner;

    @FXML
    private Spinner<Integer> minimaSpinner;

    @FXML
    private ComboBox<Produto> produtoEscolha;

    @FXML
    private Spinner<Integer> quantidadeSpinner;

    @FXML
    private Spinner<Integer> reposicaoSpinner;
    
    @FXML
    private TableView<Estoque> estoqueTabela;
    
    @FXML
    private TableColumn<Estoque, String> produtoColuna;
    
    @FXML
    private TableColumn<Estoque, String> quantidadeColuna;
    
    @FXML
    private TableColumn<Estoque, String> maximoColuna;

    @FXML
    private TableColumn<Estoque, String> minimoColuna;
    
    @FXML
    private TableColumn<Estoque, String> reposicaoColuna;
    
    @FXML
    private TableColumn<Estoque, String> statusColuna;
    @FXML
    private HBox box1;
    @FXML
    private HBox box2;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	
    	ArrayList<Produto> produtos = Produto.carregarTodosProdutos();
    	ArrayList<Estoque> estoques = Estoque.carregarTodosEstoques();
    	produtoEscolha.setItems(FXCollections.observableArrayList(produtos));
    	
    	produtoColuna.setCellValueFactory(param -> 
    		new SimpleStringProperty(param.getValue().getProduto().getNome())
    	);
    	
    	quantidadeColuna.setCellValueFactory(new PropertyValueFactory<Estoque, String>("quantidade"));
    	minimoColuna.setCellValueFactory(new PropertyValueFactory<Estoque, String>("quantidadeMinima"));
    	maximoColuna.setCellValueFactory(new PropertyValueFactory<Estoque, String>("quantidadeMaxima"));
    	reposicaoColuna.setCellValueFactory(new PropertyValueFactory<Estoque, String>("pontoReposicao"));
    	statusColuna.setCellValueFactory(new PropertyValueFactory<Estoque, String>("status"));
    	
    	estoqueTabela.setItems(FXCollections.observableArrayList(estoques));
    	
    	maximaSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));
    	minimaSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));
    	quantidadeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));
    	reposicaoSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));
    	
    	if(!Main.usuarioAutenticado.getPapel().equals("Administrador")) {
    		box1.setVisible(false);
    		box2.setVisible(false);
    		return;
    	}
    	
    	estoqueTabela.setRowFactory(param -> {
        	 TableRow<Estoque> row = new TableRow<Estoque>();
             row.setOnMouseClicked(event -> {
                 if (event.getClickCount() == 2 && !row.isEmpty()) {
                	 showEditDialog(row.getItem(), row.getIndex());
                 }
             });
             return row;
    	});
    	
    }

    @FXML
    void onClickEstoqueButton(ActionEvent event) {
    	if(quantidadeSpinner.getValue() == 0 || minimaSpinner.getValue() == 0 || maximaSpinner.getValue() == 0) {
    		DialogUtils.mostrarDialogoErro("Erro!", "Preencha todos os campos!");
    		return;
    	}
    	
    	int quantidade = quantidadeSpinner.getValue();
    	int minima = minimaSpinner.getValue();
    	int maxima = maximaSpinner.getValue();
    	int reposicao = reposicaoSpinner.getValue();
    	
    	String status = minima >= quantidade ? "Quase sem estoque" : reposicao >= quantidade ? "Precisa repor" : maxima < quantidade ? "Excesso no estoque" : "Estavel" ; 
    	
    	Estoque estoque = new Estoque(0, produtoEscolha.getValue().getCodigo(), quantidade, minima, 
    			maxima, reposicao , status);
    	
    	try {
			estoque.inserir();
			estoqueTabela.getItems().add(estoque);
	    	DialogUtils.mostrarDialogoSucesso("Sucesso!", "Estoque adicionado");
		} catch (Exception e) {
			DialogUtils.mostrarDialogoErro("Erro!", e.getMessage());
		}
    }
    
    private void showEditDialog(Estoque estoque, int index) {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Ação");
        dialog.setHeaderText("Escolha uma opção para o estoque: " + estoque.getProduto().getNome());

        ButtonType edit = new ButtonType("Editar estoque");
        ButtonType delete = new ButtonType("Deletar estoque");
        ButtonType cancelar =  new ButtonType("Cancelar");

        dialog.getButtonTypes().setAll(edit, delete, cancelar);

        dialog.showAndWait().ifPresent(response -> {
            if (response == edit) {
                 try {
                	 FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestaoestoque/fxmls/estoque/editar-estoque.fxml"));
                	 Parent root = loader.load();
                	 EditarEstoqueController controller = loader.getController();
                	 controller.loadDadosEstoque(estoque, index, estoqueTabela.getItems());
                	 Scene scene = new Scene(root);
                	 Stage stage = new Stage();
                	 stage.setTitle("Editar estoque");
                	 stage.setScene(scene);
                	 stage.initModality(Modality.APPLICATION_MODAL);
                	 stage.showAndWait();
                	 dialog.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
            } else if(response == delete) {
            	estoqueTabela.getItems().remove(estoque);
            	estoque.deletar();
            	DialogUtils.mostrarDialogoSucesso("Erro!", "Deletado com sucesso!");
           	 	dialog.close();
            } else {
            	dialog.close();
            }
        });
    }

}
