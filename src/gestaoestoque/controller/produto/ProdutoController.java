package gestaoestoque.controller.produto;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import gestaoestoque.Main;
import gestaoestoque.factory.celulas.ProdutoCelulaFactory;
import gestaoestoque.models.Fornecedor;
import gestaoestoque.models.Produto;
import gestaoestoque.utils.DialogUtils;
import gestaoestoque.utils.GeradorUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ProdutoController implements Initializable {
	
	
	@FXML
    private Button cadastrarProdutoButton;

    @FXML
    private ChoiceBox<String> categoriaEscolha;

    @FXML
    private TextField codigoBarraEntrada;

    @FXML
    private TextArea descricaoEntrada;

    @FXML
    private TextField entradaCodigo;

    @FXML
    private ChoiceBox<Fornecedor> fornecedorEscolha;

    @FXML
    private Button gerarCodigoBarraButton;

    @FXML
    private Button gerarCodigoButton;

    @FXML
    private TextField nomeEntrada;

    @FXML
    private TextField precoCompraEntrada;

    @FXML
    private TextField precoVendaEntrada;

    @FXML
    private Label previewCodigoBarra;

    @FXML
    private ListView<Produto> produtoLista;

    @FXML
    private ChoiceBox<String> unidadeMedidaEscolha;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ArrayList<Produto> produtos = Produto.carregarTodosProdutos();
    	ArrayList<Fornecedor> fornecedores = Fornecedor.carregarTodosFornecedores();
    	
    	fornecedorEscolha.getItems().addAll(fornecedores);
		ObservableList<Produto> proObservableList = FXCollections.observableArrayList(produtos);
    	
    	categoriaEscolha.setItems(FXCollections.observableArrayList("Comida", "Eletronico", "Eletrodomestico", "Pet", "Movel", "Esporte", "Infantil", "Som", "Video", "Outros"));
    	unidadeMedidaEscolha.setItems(FXCollections.observableArrayList("centimetro", "metro", "litro", "miligrama", "kilo", "mililitro", "grau", "kelvin", "volt", "ampere", "segundo", "minuto", "hora"));
    	
    	produtoLista.setItems(proObservableList);
    	produtoLista.setCellFactory(param -> new ProdutoCelulaFactory(proObservableList));
    	
    	gerarCodigoButton.setOnAction(event -> {
    		entradaCodigo.setText(GeradorUtils.gerarCodigoDeProduto());
    	});
    	
    	gerarCodigoBarraButton.setOnAction(event -> {
    		String codigoBarra = GeradorUtils.gerarCodigoDeBarras();
    		codigoBarraEntrada.setText(codigoBarra);
    		previewCodigoBarra.setText(codigoBarra);
    		previewCodigoBarra.setText(codigoBarra);
    	});
    	
    	codigoBarraEntrada.setDisable(true);
    	    	
	}
	
	@FXML
	void onClickCadastrarProduto(ActionEvent event) {
		if(!Main.usuarioAutenticado.getPapel().equals("Administrador")) {
			DialogUtils.mostrarDialogoErro("Erro!", "Não autorizado!");
			return;
		}
		if(entradaCodigo.getText().isEmpty() || nomeEntrada.getText().isEmpty() || categoriaEscolha.getValue().isEmpty() || 
				fornecedorEscolha.getValue() == null || unidadeMedidaEscolha.getValue().isEmpty() || precoCompraEntrada.getText().isEmpty() ||
				precoVendaEntrada.getText().isEmpty() || descricaoEntrada.getText().isEmpty() || codigoBarraEntrada.getText().isEmpty()) {
			DialogUtils.mostrarDialogoErro("Erro!",	"Preencha todos os campos!");
			return;
		}
		
		Produto produto = new Produto(Integer.parseInt(entradaCodigo.getText()), nomeEntrada.getText(), categoriaEscolha.getValue(), 
				fornecedorEscolha.getValue().getId(), unidadeMedidaEscolha.getValue(), Double.parseDouble(precoCompraEntrada.getText()), Double.parseDouble(precoVendaEntrada.getText()),
				descricaoEntrada.getText(), codigoBarraEntrada.getText());
		
		try {
			produto.inserir();
			produtoLista.getItems().add(produto);
			DialogUtils.mostrarDialogoSucesso("Sucesso!", "Produto adicionado a lista");
		} catch (Exception e) {
			DialogUtils.mostrarDialogoErro("Erro!", e.getMessage());
		}
	}
}
