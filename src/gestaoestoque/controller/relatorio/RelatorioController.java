package gestaoestoque.controller.relatorio;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import gestaoestoque.config.DatabaseConfig;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class RelatorioController implements Initializable {
	
	@FXML
	private Label maisVendidoLabel;
	@FXML
	private Label classeALabel;
	@FXML
	private Label classeBLabel;
	@FXML
	private Label classeCLabel;
	@FXML
	private Label baixaRotatividadeLabel;
	@FXML
	private Label altaRotatividadeLabel;
	@FXML
	private Label lucroLabel;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			String sqlMaisVendidos = "SELECT p.nome, s.produto_id, SUM(s.quantidade) AS total_quantidade "
					+ "FROM saidas s "
					+ "JOIN produtos p ON s.produto_id = p.codigo "
					+ "WHERE s.saida = 'Compra de cliente' "
					+ "GROUP BY s.produto_id, p.nome "
					+ "ORDER BY total_quantidade DESC "
					+ "LIMIT 1;";
			String sqlCurvaABC = "CALL calcular_curva_abc();";
			String sqlAltaRotatividade = "CALL produtos_alta_rotatividade()";
			String sqlBaixaRotatividade = "CALL produtos_baixa_rotatividade()";
			String sqlMargemLucro = "CALL margem_lucro_total()";

			
			PreparedStatement stmtMaisVendidos = DatabaseConfig.conexao.prepareStatement(sqlMaisVendidos);
			PreparedStatement stmtCurvaABC = DatabaseConfig.conexao.prepareStatement(sqlCurvaABC);
			PreparedStatement stmtBaixaRotatividade = DatabaseConfig.conexao.prepareStatement(sqlBaixaRotatividade);
			PreparedStatement stmtAltaRotatividade = DatabaseConfig.conexao.prepareStatement(sqlAltaRotatividade);
			PreparedStatement stmtMargemLucro = DatabaseConfig.conexao.prepareStatement(sqlMargemLucro);

			ResultSet resultMaisVendidos = stmtMaisVendidos.executeQuery();
			ResultSet resultMaisCurvaABC = stmtCurvaABC.executeQuery();
			ResultSet resultBaixaRotatividade = stmtBaixaRotatividade.executeQuery();
			ResultSet resultAltaRotatividade = stmtAltaRotatividade.executeQuery();
			ResultSet resultMargemLucro = stmtMargemLucro.executeQuery();

			
			
			while(resultMaisVendidos.next()) 
				maisVendidoLabel.setText(resultMaisVendidos.getString("nome"));
			
			while(resultMaisCurvaABC.next()) {
				if(resultMaisCurvaABC.getString("classe_abc").equals("A"))
					classeALabel.setText(resultMaisCurvaABC.getString("nome") + ", " + classeALabel.getText());
				if(resultMaisCurvaABC.getString("classe_abc").equals("B"))
					classeBLabel.setText(resultMaisCurvaABC.getString("nome") + ", " + classeBLabel.getText());
				if(resultMaisCurvaABC.getString("classe_abc").equals("C"))
					classeCLabel.setText(resultMaisCurvaABC.getString("nome") + ", " + classeCLabel.getText());
			}
			
			while(resultAltaRotatividade.next())
				altaRotatividadeLabel.setText(resultAltaRotatividade.getString("nome") + ", " + altaRotatividadeLabel.getText());
			
			while(resultBaixaRotatividade.next())
				baixaRotatividadeLabel.setText(resultBaixaRotatividade.getString("nome") + ", " + baixaRotatividadeLabel.getText());
			
			while(resultMargemLucro.next())
				lucroLabel.setText(Double.parseDouble(resultMargemLucro.getString("margem_lucro_total")) + "%");
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
