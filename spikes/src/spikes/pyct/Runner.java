package spikes.pyct;

import javax.swing.JFrame;


public class Runner {

	
	public static void main(String[] args) throws NumberFormatException {
	
		Navegacao executa = new Navegacao();
		executa.navegacao();
		executa.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		
	}

}
