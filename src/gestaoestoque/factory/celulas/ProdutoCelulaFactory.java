package gestaoestoque.factory.celulas;



import java.awt.Dialog;
import java.io.IOException;

import javax.swing.GroupLayout.Alignment;

import gestaoestoque.controller.estoque.EditarEstoqueController;
import gestaoestoque.controller.produto.EditarProdutoController;
import gestaoestoque.models.Produto;
import gestaoestoque.utils.DialogUtils;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProdutoCelulaFactory extends ListCell<Produto> {
	private final HBox hbox = new HBox();
	private final VBox vbox = new VBox();
	
	private final Label codigoLabel = new Label();
	private final Label nomeLabel = new Label();
	private final Label categoriaLabel = new Label();
	private final Label unidadeMedidaLabel = new Label();
	private final Label precoCompraLabel = new Label();
	private final Label precoVendaLabel = new Label();
	private final Label descricaoLabel = new Label();
	private final Label codigoBarraLabel = new Label();
	
    private final Button deleteButton = new Button("Excluir");
    private final ObservableList<Produto> parentList;
    
    public ProdutoCelulaFactory(ObservableList<Produto> parentList) {
        this.parentList = parentList;
        
        vbox.getChildren().addAll(codigoLabel, nomeLabel, categoriaLabel,
        		unidadeMedidaLabel, precoCompraLabel, precoVendaLabel,
        		descricaoLabel, codigoBarraLabel);
        vbox.setSpacing(5);
        
        hbox.setSpacing(100);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPadding(new Insets(10, 10, 10, 10));
        HBox.setHgrow(hbox, Priority.ALWAYS);
        HBox.setHgrow(vbox, Priority.ALWAYS);
        
        deleteButton.setOnAction(event -> {
        	Produto produto = getItem();
        	boolean isConfirm = DialogUtils.mostrarDialogoConfirmacao("Tem certeza?", "Deseja excluir este item?");
        	if(produto != null && isConfirm) {
        		try {
        			produto.deletar();
            		parentList.remove(produto);
        		} catch(Exception e) {
        			DialogUtils.mostrarDialogoErro("Erro!", e.getMessage());
        		}
        	}
        });
        
        hbox.getChildren().addAll(vbox, deleteButton);
    }
    
    @Override
    protected void updateItem(Produto item, boolean empty) {
    	super.updateItem(item, empty);
    	
    	if(empty || item == null) {
    		setGraphic(null);
    		setText(null);
    	} else {
    		codigoLabel.setText("Codigo: " + item.getCodigo());
    		codigoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    		nomeLabel.setText("Produto: " + item.getNome());
    		nomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    		categoriaLabel.setText("Categoria: " + item.getCategoria());
    		categoriaLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    		unidadeMedidaLabel.setText("Unidade de medida: " + item.getUnidadeMedida());
    		unidadeMedidaLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    		precoCompraLabel.setText("Preço de compra: " + item.getPrecoCompra());
    		precoCompraLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    		precoVendaLabel.setText("Preço de venda: " + item.getPrecoVenda());
    		precoVendaLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    		descricaoLabel.setText("Descrição: " + item.getDescricao());
    		descricaoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    		codigoBarraLabel.setText("Codigo de barras: " + item.getCodigoBarra());
    		codigoBarraLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    		
    		hbox.setOnMouseClicked(event -> {
    			if(event.getClickCount() == 2) {
    				showDialogEdit(item, getIndex());
    			}
    		});
    		
    		if (getIndex() % 2 == 0) {
                hbox.setStyle("-fx-background-color: #a8dfff;");
            } else {
                hbox.setStyle("-fx-background-color: #54c1ff;");
            }
    		
    		setGraphic(hbox);
    	}
    }
    
    void showDialogEdit(Produto item, int index) {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Editar produto");
    	alert.setHeaderText("Escolha uma opção para o produto " + item.getNome());
    	
    	ButtonType edit = new ButtonType("Editar");
    	ButtonType cancelar = new ButtonType("Cancelar");
    	
    	alert.getButtonTypes().setAll(edit, cancelar);
    	
    	alert.showAndWait().ifPresent(response -> {
        	if(response == edit) {
        		try {
               	 FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestaoestoque/fxmls/produto/editar-produto.fxml"));
               	 Parent root = loader.load();
               	 EditarProdutoController controller = loader.getController();
               	 controller.carregarDados(item, index, parentList);
               	 Scene scene = new Scene(root);
               	 Stage stage = new Stage();
               	 stage.setTitle("Editar estoque");
               	 stage.setScene(scene);
               	 stage.initModality(Modality.APPLICATION_MODAL);
               	 stage.showAndWait();
               	alert.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	} else {
        		alert.close();
        	}
        });
    }
}
