package gestaoestoque.utils;

import java.util.Random;

public class GeradorUtils {

    public static String gerarCodigoDeBarras() {
    	char[] LETRAS = {'A', 'B', 'C', 'D'};
    	Random random = new Random();
        char letra = LETRAS[random.nextInt(LETRAS.length)];

        StringBuilder numeros = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            numeros.append(random.nextInt(10));
        }

        return letra + numeros.toString() + letra;
    }
	
	public static String gerarCodigoDeProduto() {
		Random random = new Random();
        int tamanho = 8;
        StringBuilder numeroAleatorio = new StringBuilder(tamanho);
        for (int i = 0; i < tamanho; i++) {
            char digito = (char) ('0' + random.nextInt(10));
            numeroAleatorio.append(digito);
        }
        return numeroAleatorio.toString();
	}
}
