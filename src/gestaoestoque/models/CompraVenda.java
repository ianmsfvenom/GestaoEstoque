package gestaoestoque.models;

import java.util.ArrayList;

public class CompraVenda {
	Produto produto;
	int quantidade;
	double preco;
	double total;
	String tipo;
	
	public CompraVenda(Produto produto, int quantidade, double preco, double total, String tipo) {
		this.produto = produto;
		this.quantidade = quantidade;
		this.preco = preco;
		this.total = total;
		this.tipo = tipo;
	}
	
	public Produto getProduto() {
		return produto;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	public int getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	public double getPreco() {
		return preco;
	}
	public void setPreco(double preco) {
		this.preco = preco;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public static ArrayList<CompraVenda> carregarComprasVendas(ArrayList<Entrada> entradas, ArrayList<Saida> saidas) {
		ArrayList<CompraVenda> compraVendas = new ArrayList<CompraVenda>();
		
		for(Entrada entrada: entradas) {
			double total = (entrada.getQuantidade() * entrada.getProduto().getPrecoCompra()) * -1;
			compraVendas.add(new CompraVenda(entrada.getProduto(), entrada.getQuantidade(), entrada.getProduto().getPrecoCompra() * -1, total , "Compra"));
		}
		
		for(Saida saida: saidas) {
			double total = saida.getQuantidade() * saida.getProduto().getPrecoVenda();
			compraVendas.add(new CompraVenda(saida.getProduto(), saida.getQuantidade(), saida.getProduto().getPrecoVenda(), total , "Venda"));
		}
		
		return compraVendas;
	}
}
