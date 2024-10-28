package gestaoestoque.utils;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class DialogUtils {
	public static boolean mostrarDialogoConfirmacao(String titulo, String mensagem) {
        // Criando o alerta de confirmação
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null); // Sem cabeçalho
        alert.setContentText(mensagem);

        // Exibindo o diálogo e aguardando a resposta do usuário
        Optional<ButtonType> resultado = alert.showAndWait();

        // Verifica se o usuário clicou em "OK"
        return resultado.isPresent() && resultado.get() == ButtonType.OK;
    }

	public static void mostrarDialogoErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);  // Sem cabeçalho
        alert.setContentText(mensagem);
        alert.showAndWait();  // Exibe o diálogo e espera a interação do usuário
    }
	
	public static void mostrarDialogoSucesso(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);  // Sem cabeçalho
        alert.setContentText(mensagem);
        alert.showAndWait();  // Exibe o diálogo e espera a interação do usuário
    }
}
