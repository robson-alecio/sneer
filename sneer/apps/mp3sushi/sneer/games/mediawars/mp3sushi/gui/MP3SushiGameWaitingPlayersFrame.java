package sneer.games.mediawars.mp3sushi.gui;

import static wheel.i18n.Language.translate;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import sneer.games.mediawars.mp3sushi.MP3SushiGameApp;
import sneer.games.mediawars.mp3sushi.player.PlayerIdentification;
import wheel.io.ui.User;
import wheel.reactive.lists.ListSignal;


public class MP3SushiGameWaitingPlayersFrame extends javax.swing.JFrame {
	
	private static final long serialVersionUID = 1L;
	
	ListSignal<PlayerIdentification>  _players;
	private MP3SushiGameApp _game;
	private DirectoriesSelectionPane _dirPanel;
	private User _user;
	private boolean _host = false;
	
	public MP3SushiGameWaitingPlayersFrame(User user, ListSignal<PlayerIdentification> players, MP3SushiGameApp game, boolean host) {
		_players = players;
		_game = game;
		_user = user;
		_host = host;
		initComponents();
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
			public void windowClosing(WindowEvent winEvt) {
		    	closing();
		    }
		});

	}

	private void initComponents() {
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

		_dirPanel = new DirectoriesSelectionPane(_user);
		_dirPanel.getAddButton().setEnabled(_host);
		_dirPanel.getRemoveButton().setEnabled(_host);
		JScrollPane scrollpane = new JScrollPane(createPlayersList());
		scrollpane.setBackground(java.awt.Color.black);
		this.add(Mp3SwingUtil.createLabelPlusField("Directories",_dirPanel));
		this.add(Mp3SwingUtil.createLabelPlusField("Players",scrollpane));
		
		// Create a panel that uses BoxLayout.
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.add(createStartGameButton());
		buttonPane.add(Box.createHorizontalStrut(5));
		buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
		buttonPane.add(Box.createHorizontalStrut(5));
		buttonPane.add(createQuitGameButton());
		buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		buttonPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.add(buttonPane);
		
		setTitle(translate("MP3 Sushi Waiting Players"));
		setSize(600,600);

	}
	
	private JList createPlayersList() {
		final ListSignalModel<PlayerIdentification> playersListModel = new ListSignalModel<PlayerIdentification>(_players,new PlayersReceiverManager());
		final JList playersList = new JList(playersListModel);
		playersList.setBackground(java.awt.Color.black);
		playersList.setCellRenderer(new PlayerCellRenderer());
		return playersList;
	}

	private JButton createStartGameButton() {
		JButton startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				_game.startGameWithDirectories(_dirPanel.directories());
			}	

		});
		startButton.setEnabled(_host);
		return startButton;
	}

	private JButton createQuitGameButton() {
		JButton quitButton = new JButton("Quit");
		quitButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				closing();
			}

		});
		return quitButton;
	}
	private void closing() {
		_dirPanel.clearDirList();
		_game.quitGame();
	}	

}
