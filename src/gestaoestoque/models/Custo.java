package gestaoestoque.models;

public class Custo {
	private Produto produto;
	private Armazenagem armazenagem;
	private String armazem;
	private double valorCompra;
	private int quantidade;
	private double custoMedio;
	private double custoArmazenagem;
	private double valorVenda;
	
	public Custo(Produto produto, double valorCompra, int quantidade, double custoMedio, double custoArmazenagem, String armazem, double valorVenda, Armazenagem armazenagem) {
		this.produto = produto;
		this.valorCompra = valorCompra;
		this.quantidade = quantidade;
		this.custoMedio = custoMedio;
		this.custoArmazenagem = custoArmazenagem;
		this.armazem = armazem;
		this.valorVenda = valorVenda;
		this.armazenagem = armazenagem;
	}
	
	public Produto getProduto() {
		return produto;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	public double getValorCompra() {
		return valorCompra;
	}
	public void setValorCompra(double valorCompra) {
		this.valorCompra = valorCompra;
	}
	public int getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	public double getCustoMedio() {
		return custoMedio;
	}
	public void setCustoMedio(double custoMedio) {
		this.custoMedio = custoMedio;
	}
	public double getCustoArmazenagem() {
		return custoArmazenagem;
	}
	public void setCustoArmazenagem(double custoArmazenagem) {
		this.custoArmazenagem = custoArmazenagem;
	}
	public double getValorVenda() {
		return valorVenda;
	}
	public void setValorVenda(double valorVenda) {
		this.valorVenda = valorVenda;
	}
	public String getArmazem() {
		return armazem;
	}
	public void setArmazem(String armazem) {
		this.armazem = armazem;
	}
	public Armazenagem getArmazenagem() {
		return armazenagem;
	}
	public void setArmazenagem(Armazenagem armazenagem) {
		this.armazenagem = armazenagem;
	}
}
