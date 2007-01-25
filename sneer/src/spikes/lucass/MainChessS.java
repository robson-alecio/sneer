package spikes.lucass;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;


public class MainChessS{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public static void main(String args[]){
		GamePanel cb= new GamePanel();
		
		JFrame frameTeste= new JFrame();
		
		JMenuBar teste= new JMenuBar();
		JMenu file= new JMenu("teste");
		teste.add(file);
		
		frameTeste.setJMenuBar(teste);
		frameTeste.add(cb);
		frameTeste.setSize(500,500);
		frameTeste.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameTeste.setVisible(true);
	}
	 
}
