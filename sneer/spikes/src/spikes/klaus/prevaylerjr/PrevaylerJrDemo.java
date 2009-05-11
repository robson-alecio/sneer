package spikes.klaus.prevaylerjr;

import java.io.IOException;


public class PrevaylerJrDemo {

	public static void main(String[] args) throws IOException {
		
		Console console = new Console();
		Sistema sistema = PrevaylerJr.tornarInvulneravel(new CadastroDeNomes());

		console.operar(sistema);
	}

}









