package gestaoestoque.controller.estoque;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import gestaoestoque.models.Estoque;
import gestaoestoque.models.Produto;
import gestaoestoque.utils.DialogUtils;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;

public class EditarEstoqueController implements Initializable {

    @FXML
    private Button editarEstoqueButton;

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
    private Estoque estoque;
    
    @FXML
    private int index;
    
    @FXML
	private ObservableList<Estoque> estoques;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	ArrayList<Produto> produtos = Produto.carregarTodosProdutos();
    	ArrayList<Estoque> estoques = Estoque.carregarTodosEstoques();
    	produtoEscolha.setItems(FXCollections.observableArrayList(produtos));
    	
    	maximaSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));
    	minimaSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));
    	quantidadeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));
    	reposicaoSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));
    	
    }
    
    @FXML
    void onClickEstoqueButton(ActionEvent event) {
    	estoque.setPontoReposicao(reposicaoSpinner.getValue());
    	estoque.setProdutoId(produtoEscolha.getValue().getCodigo());
    	estoque.setQuantidade(quantidadeSpinner.getValue());
    	estoque.setQuantidadeMaxima(maximaSpinner.getValue());
    	estoque.setQuantidadeMinima(minimaSpinner.getValue());
    	
    	if(estoque.salvar()) {
    		DialogUtils.mostrarDialogoSucesso("Sucesso!", "Estoque atualizado!");
    		estoques.set(index, estoque);
    	} else 
    		DialogUtils.mostrarDialogoErro("Erro!", "Falha ao atualizar estoque");
    	
    }
    
    public void loadDadosEstoque(Estoque estoque, int index, ObservableList<Estoque> estoques) {
		this.estoque = estoque;
		this.index = index;
		this.estoques = estoques;
		produtoEscolha.setValue(estoque.getProduto());
    	quantidadeSpinner.getValueFactory().setValue(estoque.getQuantidade());
    	minimaSpinner.getValueFactory().setValue(estoque.getQuantidadeMinima());
    	maximaSpinner.getValueFactory().setValue(estoque.getQuantidadeMaxima());
    	reposicaoSpinner.getValueFactory().setValue(estoque.getPontoReposicao());
	}

}
