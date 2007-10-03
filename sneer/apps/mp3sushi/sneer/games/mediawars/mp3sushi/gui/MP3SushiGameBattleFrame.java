package sneer.games.mediawars.mp3sushi.gui;

import static wheel.i18n.Language.translate;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Timer;

import sneer.games.mediawars.mp3sushi.ID3Summary;
import sneer.games.mediawars.mp3sushi.player.PlayerIdentification;
import sneer.games.mediawars.mp3sushi.round.MP3SushiRound;
import wheel.reactive.lists.ListSignal;


public class MP3SushiGameBattleFrame extends javax.swing.JFrame implements KeyListener {
	
	private static final long serialVersionUID = 1L;
	JTextField _guess = new JTextField(100);
	ListSignal<PlayerIdentification> _players;
	ArrayList<ID3Summary> _mp3sToGuess;
	final JFrame _countDownFrame = new JFrame("COUNTDOWN");
	final JLabel label = new JLabel();
	
	volatile boolean _countDownFinished = false;
	Timer _countdownTimer;
	Object _countDownLock = new Object();
	
	volatile boolean _gameOver = false;
	Object _gameOverLock = new Object();
	
	private ListModelStringFilter<ID3Summary> _listModelStringFilter;
	private MP3SushiRound _mp3SushiRound;
	
	public MP3SushiGameBattleFrame(ListSignal<PlayerIdentification> players, ArrayList<ID3Summary> mp3sToGuess) {
		_players = players;
		_mp3sToGuess = mp3sToGuess;
		initComponents();
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
			public void windowClosing(WindowEvent winEvt) {
		    	_mp3SushiRound.quitGame();
		    }
		});

	}

	private void initComponents() {
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

		this.add(Mp3SwingUtil.createLabelPlusField("Guess - hit <Enter> when done",_guess));

		_guess.addKeyListener(this);
		disableGuess();
		
		_listModelStringFilter = new ListModelStringFilter<ID3Summary>(_mp3sToGuess, _guess);
		JList _list = new JList(_listModelStringFilter);
		JScrollPane scrollpaneOfMp3s = new JScrollPane(_list);
		scrollpaneOfMp3s.setBackground(java.awt.Color.black);
		this.add(Mp3SwingUtil.createLabelPlusField("Possible Mp3s", scrollpaneOfMp3s));

		JScrollPane scrollpane = new JScrollPane(createPlayersList());
		scrollpane.setBackground(java.awt.Color.black);
		this.add(Mp3SwingUtil.createLabelPlusField("Scores", scrollpane));
		
		setTitle(translate("MP3 Sushi War Zone"));
		setSize(600,500);
		
		_countDownFrame.add(label);
		_countDownFrame.setSize(100, 100);
		_countDownFrame.setLocation(250, 250);
		label.setFont(new Font("Serif", Font.PLAIN, 50));
	}
	
	private JList createPlayersList() {
		final ListSignalModel<PlayerIdentification> playersListModel = new ListSignalModel<PlayerIdentification>(_players,new ScorePlayersReceiverManager());
		final JList playersList = new JList(playersListModel);
		playersList.setBackground(java.awt.Color.black);
		playersList.setCellRenderer(new ScorePlayerCellRenderer());
		return playersList;
	}

	public void startCountDown() {
		_countDownFinished = false;
		_countdownTimer = new Timer(1000, new ActionListener() {
			int count = 5;
			@Override
			public void actionPerformed(ActionEvent e) {
				_countDownFrame.setVisible(true);
				label.setText("  "+count);
				count--;
				if (count == -1) {
					synchronized (_countDownLock) {
						_countdownTimer.stop();
						_countDownFrame.setVisible(false);
						_guess.setEnabled(true);
						_guess.setText("");
						_guess.requestFocusInWindow();
						_listModelStringFilter.refreshList();
						_countDownFinished = true;
						_countDownLock.notify();
					}
				} 
			}
			
		});
		_countdownTimer.start();
	}

	public Boolean isCountDownFinished() {
		return _countDownFinished;
	}
	
	public void resetCountDownFinished() {
		_countDownFinished = false;
	}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (_listModelStringFilter.getSize()==1) {
				_mp3SushiRound.setAnswer(_listModelStringFilter.getElementAt(0),System.nanoTime());
				disableGuess();
			}
		}

	}

	public void disableGuess() {
		_guess.setEnabled(false);
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	public Object getCountDownLock() {
		return _countDownLock;
	}

	public void setMp3SushiRound(MP3SushiRound mp3SushiRound) {
		_mp3SushiRound = mp3SushiRound;
	}

	public void showGameOver() {
		JOptionPane.showMessageDialog(this,"GAME OVER", "Message", JOptionPane.PLAIN_MESSAGE);
		synchronized (_gameOverLock) {
			_gameOver = true;
			_gameOverLock.notify();
		}
	}

	public Object getGameOverLock() {
		return _gameOverLock;
	}

	public boolean isGameOver() {
		return _gameOver;
	}
	
	
}
