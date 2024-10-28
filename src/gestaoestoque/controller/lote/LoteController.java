package gestaoestoque.controller.lote;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.ResourceBundle;

import gestaoestoque.Main;
import gestaoestoque.models.Lote;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LoteController implements Initializable {
	
	@FXML
	private ChoiceBox<Produto> produtoEscolha;
	@FXML
	private TextField loteField;
	@FXML
	private DatePicker validadeDate;
	@FXML
	private Spinner<Integer> quantidadeSpinner;
	@FXML
	private Button cadastrarLoteButton;
	@FXML
	private TableView<Lote> loteTable;
	@FXML
	private TableColumn<Lote, String> produtoColuna;
	@FXML
	private TableColumn<Lote, String> quantidadeColuna;
	@FXML
	private TableColumn<Lote, String> loteColuna;
	@FXML
	private TableColumn<Lote, String> validadeColuna;
	@FXML
	private HBox box1;
	@FXML
	private VBox box2;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ArrayList<Produto> produtos = Produto.carregarTodosProdutos();
		ArrayList<Lote> lotes = Lote.carregarTodosLotes();
		
		produtoEscolha.setItems(FXCollections.observableArrayList(produtos));
		
		validadeColuna.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDataValidade().toString()));
		loteColuna.setCellValueFactory(new PropertyValueFactory<Lote, String>("nome"));
		quantidadeColuna.setCellValueFactory(new PropertyValueFactory<Lote, String>("quantidade"));
		produtoColuna.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getProduto().getNome()));
    	quantidadeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));

		loteTable.setItems(FXCollections.observableArrayList(lotes));
		
		if(!Main.usuarioAutenticado.getPapel().equals("Administrador")) {
			box1.setVisible(false);
			box2.setVisible(false);
			cadastrarLoteButton.setVisible(false);
			return;
		}
		
		loteTable.setRowFactory(param -> {
			TableRow<Lote> row = new TableRow<Lote>();
			row.setOnMouseClicked(event -> {
				if(event.getClickCount() == 2 && row != null) 
					showDialogDelete(row.getItem(), row.getIndex());
			});
			return row;
		});
	}
	
	@FXML
	public void onClickLoteButton(ActionEvent event) {
		if(produtoEscolha.getValue() == null || loteField.getText().isEmpty() || quantidadeSpinner.getValue() == 0) {
			DialogUtils.mostrarDialogoErro("Erro!", "Preencha todos os campos!");
			return;
		}
		
		LocalDate date = validadeDate.getValue();
		Date dataValidade = Date.valueOf(date);
		
		Lote lote = new Lote(0, produtoEscolha.getValue().getCodigo(), quantidadeSpinner.getValue(), loteField.getText(), dataValidade);
		try {
			lote.inserir();
			ArrayList<Lote> lotes = Lote.carregarTodosLotes();
			loteTable.setItems(FXCollections.observableArrayList(lotes));
			DialogUtils.mostrarDialogoSucesso("Sucesso!", "Lote inserido com sucesso!");

		} catch (Exception e) {
			DialogUtils.mostrarDialogoErro("Erro!", e.getMessage());
		}
	}
	
	public void showDialogDelete(Lote item, int index) {
		Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Ação");
        dialog.setHeaderText("Deseja deletar o lote do produto " + item.getProduto().getNome() + "?");

        ButtonType delete = new ButtonType("Deletar lote");
        ButtonType cancelar =  new ButtonType("Cancelar");
        
        dialog.getButtonTypes().setAll(delete, cancelar);
        
        dialog.showAndWait().ifPresent(response -> {
        	if(response == delete) {
        		try {
					item.deletar();
					loteTable.getItems().remove(item);
					DialogUtils.mostrarDialogoSucesso("Sucesso!", "Lote deletado com sucesso!");
				} catch (Exception e) {
					e.printStackTrace();
					DialogUtils.mostrarDialogoErro("Erro!", "Falha ao deletar lote");
				}
        	} else {
        		dialog.close();
        	}
        });
	}
}
