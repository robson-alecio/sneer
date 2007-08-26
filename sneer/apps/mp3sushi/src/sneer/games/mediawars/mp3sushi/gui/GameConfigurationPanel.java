package sneer.games.mediawars.mp3sushi.gui;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import sneer.games.mediawars.mp3sushi.GameConfiguration;
import sneer.games.mediawars.mp3sushi.round.Mp3PieceProvider;

public class GameConfigurationPanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField _theme = new JTextField("New Theme",40);
	private JTextField _secondsToGuess = new JTextField("10",40);
	private JTextField _secondsOfMusic = new JTextField("10",40);
	private JTextField _rounds = new JTextField("5",40);
	private ButtonGroup _type = new ButtonGroup();
	private JRadioButton _begining = new JRadioButton("Begining");
	private JRadioButton _ending = new JRadioButton("Ending");
	private JRadioButton _random = new JRadioButton("Random");
	private int _intType = Mp3PieceProvider.BEGINING;
	
	public GameConfigurationPanel() {
		this.initializeItems();
	}
	
	public void createTypeGroup() {
		_type.add(_begining);
		_begining.setActionCommand("begining");
		_begining.addActionListener(this);
		_begining.setSelected(true);
		_type.add(_ending);
		_ending.setActionCommand("ending");
		_ending.addActionListener(this);
		_type.add(_random);
		_random.setActionCommand("random");
		_random.addActionListener(this);
	}

	public void initializeItems() {
		this.createTypeGroup();
		BoxLayout bl = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(bl);
		
		_theme.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.add(this.label("Theme"));
		this.add(_theme);
		
		_secondsToGuess.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.add(this.label("Seconds to guess"));
		this.add(_secondsToGuess);

		_secondsOfMusic.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.add(this.label("Seconds of music"));
		this.add(_secondsOfMusic);

		_rounds.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.add(this.label("Rounds"));
		this.add(_rounds);

		this.add(this.label("Part of mp3 "));
		JPanel radioPanel = new JPanel(new GridLayout(0, 1));
        radioPanel.add(_begining);
        radioPanel.add(_ending);
        radioPanel.add(_random);
        radioPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.add(radioPanel);
	}

	private JLabel label(String string) {
		JLabel jl = new JLabel(string);
		jl.setAlignmentX(Component.LEFT_ALIGNMENT);
		return jl;
	}

	public JTextField getTheme() {
		return _theme;
	}

	public JTextField getSecondsToGuess() {
		return _secondsToGuess;
	}

	public JTextField getSecondsOfMusic() {
		return _secondsOfMusic;
	}

	public ButtonGroup getType() {
		return _type;
	}

	public GameConfiguration getConfiguration() {
		String theme = _theme.getText();
		Integer secondsToGuess = new Integer(_secondsToGuess.getText());
		Integer secondsOfMusic = new Integer(_secondsOfMusic.getText());
		Integer rounds = new Integer(_rounds.getText());
		return new GameConfiguration(theme, _intType, secondsOfMusic, secondsToGuess, rounds);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("begining")) _intType = Mp3PieceProvider.BEGINING;
		if (e.getActionCommand().equals("ending")) _intType = Mp3PieceProvider.ENDING;
		if (e.getActionCommand().equals("random")) _intType = Mp3PieceProvider.RANDOM;
		
	}
	
}
