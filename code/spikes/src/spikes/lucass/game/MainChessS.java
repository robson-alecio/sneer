package spikes.lucass.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import spikes.lucass.game.type.ChessOptions;
import spikes.lucass.game.type.GoOptions;


public class MainChessS{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;	
	
	public static void main(String args[]){
		new GameFrame();
	}
	 
}

class GameFrame{	
	
	private GamePanel _gamePanel;
	
	public GameFrame() {		
		JFrame gameFrame= new JFrame();
		
		createMenu(gameFrame);
		createGamePanel(gameFrame);
		
		gameFrame.setSize(500,500);
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setVisible(true);
	}

	private void createGamePanel(JFrame gameFrame) {
		_gamePanel= new GamePanel(new ChessOptions());
		gameFrame.add(_gamePanel);
	}

	private void createMenu(JFrame gameFrame) {
		JMenuBar mainMenuBar= new JMenuBar();
		
			JMenu subMenuOpcoes= new JMenu(Resources.getString(Resources.OPTIONS));
			
				JMenu subMenuMudarJogo= new JMenu(Resources.getString(Resources.CHANGE_GAME));
				
					JMenuItem subMenuXadrez= new JMenuItem(Resources.getString(Resources.CHESS));
					subMenuXadrez.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent action) {
							_gamePanel.setGame(new ChessOptions());
						}
					});
					subMenuMudarJogo.add(subMenuXadrez);
					
					JMenuItem subMenuGo= new JMenuItem(Resources.getString(Resources.GO));
					subMenuGo.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent action) {
							_gamePanel.setGame(new GoOptions());
						}
					});
					subMenuMudarJogo.add(subMenuGo);
				
				subMenuOpcoes.add(subMenuMudarJogo);
				
			mainMenuBar.add(subMenuOpcoes);
						
			mainMenuBar.add(new JMenu(Resources.getString(Resources.ABOUT)));
			
		gameFrame.setJMenuBar(mainMenuBar);
	}
}
