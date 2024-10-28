package gestaoestoque.models;

public abstract class BaseModel {
	public abstract String getNomeTabela();
	
	public abstract void deletar() throws Exception;
	public abstract void inserir() throws Exception;
	public boolean salvar() throws Exception {
		return false;
	}
}
