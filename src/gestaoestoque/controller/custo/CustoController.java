package gestaoestoque.controller.custo;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import gestaoestoque.models.Armazenagem;
import gestaoestoque.models.Custo;
import gestaoestoque.models.Entrada;
import gestaoestoque.models.Estoque;
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

public class CustoController implements Initializable {
	
	@FXML
	private ChoiceBox<Produto> produtoEscolha;
	@FXML
	private Spinner<Integer> quantidadeSpinner;
	@FXML
	private TextField valorArmazenagemField;
	@FXML
	private TableColumn<Custo, String> produtoColuna;
	@FXML
	private TableColumn<Custo, String> valorCompraColuna;
	@FXML
	private TableColumn<Custo, String> quantidadeColuna;
	@FXML
	private TableColumn<Custo, String> armazemColuna;
	@FXML
	private TableColumn<Custo, String> custoMedioColuna;
	@FXML
	private TableColumn<Custo, String> custoArmazenagemColuna;
	@FXML
	private TableColumn<Custo, String> valorVendaColuna;
	@FXML
	private TableView<Custo> custosTable;
	@FXML
	private TextField armazemField;
	@FXML
	private Button cadastrarCustoButton;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ArrayList<Produto> produtos = Produto.carregarTodosProdutos();
		ArrayList<Armazenagem> armazenagens = Armazenagem.carregarTodasArmazenagens();
		produtoEscolha.setItems(FXCollections.observableArrayList(produtos));
		
		produtoColuna.setCellValueFactory(param -> 
				new SimpleStringProperty(param.getValue().getProduto().getNome()));
		
		valorCompraColuna.setCellValueFactory(new PropertyValueFactory<Custo,String>("valorCompra"));
		quantidadeColuna.setCellValueFactory(new PropertyValueFactory<Custo, String>("quantidade"));
		custoMedioColuna.setCellValueFactory(new PropertyValueFactory<Custo, String>("custoMedio"));
		custoArmazenagemColuna.setCellValueFactory(new PropertyValueFactory<Custo, String>("custoArmazenagem"));
		armazemColuna.setCellValueFactory(new PropertyValueFactory<Custo, String>("armazem"));
		valorVendaColuna.setCellValueFactory(new PropertyValueFactory<Custo, String>("valorVenda"));
    	quantidadeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));

    	ArrayList<Custo> custos = calcularCustos(armazenagens);
    	custosTable.setItems(FXCollections.observableArrayList(custos));
    	
    	custosTable.setRowFactory(param -> {
    		TableRow<Custo> row = new TableRow<Custo>();
    		row.setOnMouseClicked(event -> {
    			if(event.getClickCount() == 2 && row != null) {
					showDoubleClickDialog(row.getItem(), row.getIndex());
    			}
    		});
    		return row;
    	});
	}
	
	@FXML
	public void onClickCustoButton(ActionEvent event) {
		if(valorArmazenagemField.getText().isEmpty() || armazemField.getText().isEmpty() || Double.isNaN(Double.parseDouble(valorArmazenagemField.getText())) || 
					quantidadeSpinner.getValue() == 0 || produtoEscolha.getValue() == null) {
			DialogUtils.mostrarDialogoErro("Erro!", "Preencha todos os campos!");
			return;
		}
		
		Armazenagem armazenagem = new Armazenagem(0, produtoEscolha.getValue().getCodigo(), quantidadeSpinner.getValue(),
				Double.parseDouble(valorArmazenagemField.getText()), armazemField.getText());
		
		try {
			armazenagem.inserir();
			ArrayList<Armazenagem> armazenagens = Armazenagem.carregarTodasArmazenagens();
			ArrayList<Custo> custos = calcularCustos(armazenagens);
	    	custosTable.setItems(FXCollections.observableArrayList(custos));

		} catch (Exception e) {
			DialogUtils.mostrarDialogoErro("Erro!", e.getMessage());
		}
	}
	
	public ArrayList<Custo> calcularCustos(ArrayList<Armazenagem> armazenagens) {
		ArrayList<Custo> custos = new ArrayList<Custo>();
		
		for(Armazenagem armazenagem: armazenagens) {
			double custoArmazenamento = armazenagem.getValor();
			double precoCompraProduto = armazenagem.getProduto().getPrecoCompra();
			Produto produto = armazenagem.getProduto();
			int quantidade = armazenagem.getQuantidade();
			String nomeArmazem = armazenagem.getNome();
			double custoMedio = (custoArmazenamento + (precoCompraProduto * quantidade)) / quantidade;
			double custoVenda = custoMedio * 2;
			
			Custo custo = new Custo(produto, precoCompraProduto, quantidade, custoMedio, custoArmazenamento, nomeArmazem, custoVenda, armazenagem);
			custos.add(custo);
		}
		
		return custos;
	}
	
	private void showDoubleClickDialog(Custo item, int index) {
    	Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Ação");
        dialog.setHeaderText("Deseja deletar o custo de armazenagem para " + item.getProduto().getNome() + "?");

        ButtonType delete = new ButtonType("Deletar");
        ButtonType cancelar =  new ButtonType("Cancelar");
        
        dialog.getButtonTypes().setAll(delete, cancelar);
        
        dialog.showAndWait().ifPresent(response -> {
        	if(response == delete) {
        		try {
					item.getArmazenagem().deletar();
					custosTable.getItems().remove(item);
					DialogUtils.mostrarDialogoSucesso("Sucesso!", "Custo deletado com sucesso!");
				} catch (Exception e) {
					e.printStackTrace();
					DialogUtils.mostrarDialogoErro("Erro!", "Falha ao deletar custo");
				}
        	} else {
        		dialog.close();
        	}
        });
	}
}
