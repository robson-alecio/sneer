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
		GamePanel gamePanel= new GamePanel();
		
		JFrame gameFrame= new JFrame();
		
		JMenuBar mainMenuBar= new JMenuBar();
		JMenu subMenuOpcoes= new JMenu("Opções");
		subMenuOpcoes.add(new JMenu("Mudar jogo"));
		mainMenuBar.add(subMenuOpcoes);
		
		gameFrame.setJMenuBar(mainMenuBar);
		gameFrame.add(gamePanel);
		gameFrame.setSize(500,500);
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setVisible(true);
	}
	 
}
