package gestaoestoque.controller.compra_venda;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import gestaoestoque.models.CompraVenda;
import gestaoestoque.models.Entrada;
import gestaoestoque.models.Saida;
import gestaoestoque.utils.DialogUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class CompraVendaController implements Initializable {

	@FXML
	private TableView<CompraVenda> compraVendaTable;
	@FXML
	private TableColumn<CompraVenda, String> produtoColuna;
	@FXML
	private TableColumn<CompraVenda, String> quantidadeColuna;
	@FXML
	private TableColumn<CompraVenda, String> precoColuna;
	@FXML
	private TableColumn<CompraVenda, String> totalColuna;
	@FXML
	private TableColumn<CompraVenda, String> tipoColuna;
	@FXML
	private Label comprasLabel;
	@FXML
	private Label vendasLabel;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			ArrayList<Saida> saidas = Saida.procurarMuitosPorSaida("Compra de cliente");
			ArrayList<Entrada> entradas = Entrada.procurarMuitosPorEntrada("Compra de fornecedor");
			ArrayList<CompraVenda> comprasVendas = CompraVenda.carregarComprasVendas(entradas, saidas);
			
			produtoColuna.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getProduto().getNome()));
			quantidadeColuna.setCellValueFactory(new PropertyValueFactory<CompraVenda, String>("quantidade"));
			precoColuna.setCellValueFactory(new PropertyValueFactory<CompraVenda, String>("preco"));
			totalColuna.setCellValueFactory(new PropertyValueFactory<CompraVenda, String>("total"));
			tipoColuna.setCellValueFactory(new PropertyValueFactory<CompraVenda, String>("tipo"));
			
			compraVendaTable.setItems(FXCollections.observableArrayList(comprasVendas));
			
			comprasLabel.setText(Integer.toString(entradas.size()));
			vendasLabel.setText(Integer.toString(saidas.size()));
			
			
		} catch (Exception e) {
			DialogUtils.mostrarDialogoErro("Erro!", e.getMessage());
		}
	}
}
