package sneer.games.mediawars.mp3sushi.gui;

import static wheel.i18n.Language.translate;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import sneer.games.mediawars.mp3sushi.MP3SushiGameApp;
import sneer.games.mediawars.mp3sushi.TheirsGame;
import wheel.reactive.lists.ListSignal;


public class MP3SushiGameStartUpFrame extends javax.swing.JFrame {
	
	private static final long serialVersionUID = 1L;
	
	ListSignal<TheirsGame>  _games;

	private MP3SushiGameApp _myGame;
	private GameConfigurationPanel _editPanel;
	
	public MP3SushiGameStartUpFrame(ListSignal<TheirsGame> listSignal, MP3SushiGameApp game) {
		_games = listSignal;
		_myGame = game;
		initComponents();
	}

	private void initComponents() {
		this.setLayout(new BorderLayout());

		_editPanel = new GameConfigurationPanel();
		_editPanel.add(createStartGameButton(), BorderLayout.EAST);
		JScrollPane scrollpane = new JScrollPane(createGamesList());
		scrollpane.setBackground(java.awt.Color.black);
		this.add(scrollpane, BorderLayout.NORTH);
		this.add(_editPanel, BorderLayout.CENTER);

		setTitle(translate("MP3 Sushi Table"));
		setSize(600,550);

	}
	
	private JList createGamesList() {
		final ListSignalModel<TheirsGame> gamesListModel = new ListSignalModel<TheirsGame>(_games,new GamesReceiverManager());
		final JList gamesList = new JList(gamesListModel);
		gamesList.setBackground(java.awt.Color.black);
		gamesList.setCellRenderer(new GameCellRenderer());
		
		gamesList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				final boolean rightClick = mouseEvent.getButton() == MouseEvent.BUTTON3;
				if (rightClick){
					int indexUnderMouse = gamesList.locationToIndex(mouseEvent.getPoint());
					if (indexUnderMouse == -1)
						return;
					
					gamesList.setSelectedIndex(indexUnderMouse);
					
					getGamePopUpMenu(gamesList)
						.show(gamesList, mouseEvent.getX(), mouseEvent.getY());
				}
			}
		});
		
		return gamesList;
	}

	private JPopupMenu getGamePopUpMenu(final JList gamesList) {
		final JPopupMenu menu = new JPopupMenu();
		final JMenuItem item = new JMenuItem("Join game");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ignored) {
				TheirsGame game = _games.currentGet(gamesList.getSelectedIndex());
				_myGame.joinGame(game);
			}
		});
		
		menu.add(item);
		return menu;
	}
	
	private JButton createStartGameButton() {
		JButton addButton = new JButton("Start");
		addButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				_myGame.waitingPlayerForConfiguration(_editPanel.getConfiguration());
			}	

		});
		return addButton;
	}
	
}
