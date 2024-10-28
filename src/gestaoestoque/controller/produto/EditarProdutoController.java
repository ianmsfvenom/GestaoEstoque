package gestaoestoque.controller.produto;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class EditarProdutoController implements Initializable {
	private Produto produto;
	private int index;
	private ObservableList<Produto> produtos;
	
	@FXML
    private ChoiceBox<String> categoriaEscolha;
    @FXML
    private TextField codigoBarraEntrada;
    @FXML
    private TextArea descricaoEntrada;
    @FXML
    private Button edicaoButton;
    @FXML
    private TextField entradaCodigo;
    @FXML
    private ChoiceBox<Fornecedor> fornecedorEscolha;
    @FXML
    private Button gerarCodigoBarraButton;
    @FXML
    private TextField nomeEntrada;
    @FXML
    private TextField precoCompraEntrada;
    @FXML
    private TextField precoVendaEntrada;
    @FXML
    private Label previewCodigoBarra;
    @FXML
    private ChoiceBox<String> unidadeMedidaEscolha;

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
    	ArrayList<Fornecedor> fornecedores = Fornecedor.carregarTodosFornecedores();
    	fornecedorEscolha.getItems().addAll(fornecedores);
    	categoriaEscolha.setItems(FXCollections.observableArrayList("Comida", "Eletronico", "Eletrodomestico", "Pet", "Movel", "Esporte", "Infantil", "Som", "Video", "Outros"));
    	unidadeMedidaEscolha.setItems(FXCollections.observableArrayList("centimetro", "metro", "litro", "miligrama", "kilo", "mililitro", "grau", "kelvin", "volt", "ampere", "segundo", "minuto", "hora"));
    	
    	
    	gerarCodigoBarraButton.setOnAction(event -> {
    		String codigoBarra = GeradorUtils.gerarCodigoDeBarras();
    		codigoBarraEntrada.setText(codigoBarra);
    		previewCodigoBarra.setText(codigoBarra);
    		previewCodigoBarra.setText(codigoBarra);
    	});
    	
    	codigoBarraEntrada.setDisable(true);
    	entradaCodigo.setEditable(false);
	}
	
	public void carregarDados(Produto produto, int index, ObservableList<Produto> produtos) {
		this.produto = produto;
		this.index = index;
		this.produtos = produtos;
		
		entradaCodigo.setText(Integer.toString(produto.getCodigo()));
		categoriaEscolha.setValue(produto.getCategoria());
		codigoBarraEntrada.setText(produto.getCodigoBarra());
		previewCodigoBarra.setText(produto.getCodigoBarra());
		descricaoEntrada.setText(produto.getDescricao());
		fornecedorEscolha.setValue(produto.getFornecedor());
		nomeEntrada.setText(produto.getNome());
		precoCompraEntrada.setText(Double.toString(produto.getPrecoCompra()));
		precoVendaEntrada.setText(Double.toString(produto.getPrecoVenda()));
		unidadeMedidaEscolha.setValue(produto.getUnidadeMedida());

	}
	
	@FXML
    void onClickEdicaoButton(ActionEvent event) {
		
		produto.setNome(nomeEntrada.getText());
		produto.setCategoria(categoriaEscolha.getValue());
		produto.setFornecedorId(fornecedorEscolha.getValue().getId());
		produto.setUnidadeMedida(unidadeMedidaEscolha.getValue());
		produto.setPrecoCompra(Double.parseDouble(precoCompraEntrada.getText()));
		produto.setPrecoVenda(Double.parseDouble(precoVendaEntrada.getText()));
		produto.setDescricao(descricaoEntrada.getText());
		produto.setCodigoBarra(codigoBarraEntrada.getText());
		
		try {
			produto.salvar();
			produtos.set(index, produto);
			DialogUtils.mostrarDialogoSucesso("Sucesso!", "Produto editado com sucesso!");
		} catch (Exception e) {
			DialogUtils.mostrarDialogoErro("Erro!", e.getMessage());
		}
		
    }
}
