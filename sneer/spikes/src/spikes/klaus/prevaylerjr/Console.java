package spikes.klaus.prevaylerjr;

import java.util.Scanner;

public class Console { //COMPLEXIDADE ACIDENTAL

	public void operar(Sistema sistema) { 
		while (true) {
			String resultado = sistema.processa(proximoComando());
			System.out.println(resultado);
		}
	}

	private static String proximoComando() {
		return new Scanner(System.in).next();
	}

}
