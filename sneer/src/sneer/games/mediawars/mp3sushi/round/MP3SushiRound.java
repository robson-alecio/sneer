package sneer.games.mediawars.mp3sushi.round;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;

import javax.swing.SwingUtilities;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import sneer.games.mediawars.mp3sushi.GameConfiguration;
import sneer.games.mediawars.mp3sushi.ID3Summary;
import sneer.games.mediawars.mp3sushi.MP3SushiGameApp;
import sneer.games.mediawars.mp3sushi.MyGame;
import sneer.games.mediawars.mp3sushi.gui.MP3SushiGameBattleFrame;
import sneer.games.mediawars.mp3sushi.player.PlayerIdentification;

public abstract class MP3SushiRound extends Thread {

	protected MP3SushiGameApp _mp3SushiGameApp;
	MP3SushiGameBattleFrame _gameBattle;
	protected MyGame _myGame;
	protected ID3Summary _actualAnswer;
	protected long _roundStartTime;
	AdvancedPlayer _mp3Player;
	protected byte[] _nextMp3s;
	protected long _actualAnswerTime;
	protected int _round = 0;
	protected int _rounds;

	public MP3SushiRound(MP3SushiGameApp mp3SushiGameApp,
			MP3SushiGameBattleFrame gameBattle, MyGame myGame) {
		_mp3SushiGameApp = mp3SushiGameApp;
		_gameBattle = gameBattle;
		_myGame = myGame;
		_rounds = myGame.getGameConfiguration().output().currentValue().getRounds();
	}

	@Override
	public void run() {
		try {
			while (_round < _rounds) {
				_round ++;
				preRound();
				startRound();
				playRound();
				afterRound();
			}
			SwingUtilities.invokeLater(new Runnable() { public void run() {_gameBattle.showGameOver();}});
			synchronized (_gameBattle.getGameOverLock()) {
				while (!_gameBattle.isGameOver()) {_gameBattle.getGameOverLock().wait();}
			}
		} catch (InterruptedException e) {
			if (_mp3Player!= null) _mp3Player.close();
			interruptGame();
		}
		_mp3SushiGameApp.showStartUpFrame();
	}


	public abstract void preRound() throws InterruptedException;
	
	public void startRound() throws InterruptedException {
		for (int i = 0; i < _myGame.getPlayers().currentSize(); i++) {
			PlayerIdentification pi = _myGame.getPlayers().currentGet(i);
			pi.resetActualScore();
		}
		waitCountdown();
	}

	private void waitCountdown() throws InterruptedException {
		_gameBattle.resetCountDownFinished();
		SwingUtilities.invokeLater(
			new Runnable() {
				public void run() {
					_gameBattle.startCountDown();
				}
			}
		);
		synchronized (_gameBattle.getCountDownLock()) {
			while (!_gameBattle.isCountDownFinished()) _gameBattle.getCountDownLock().wait();
		}
	}

	public void playRound() throws InterruptedException {
		_actualAnswer = null;
		GameConfiguration g = _myGame.getGameConfiguration().output().currentValue();
		try {
			_mp3Player = new AdvancedPlayer(new BufferedInputStream(new ByteArrayInputStream(_nextMp3s)));
		} catch (JavaLayerException e1) {
			e1.printStackTrace();
		}
		new Thread() {
			@Override
			public void run() {
				try {
					_mp3Player.play();
				} catch (JavaLayerException e) {
					e.printStackTrace();
				}
			}
		}.start();
		_roundStartTime = System.nanoTime();
		sleep((g.getSecondsOfMusic()+g.getSecondsToGuess()) * 1000);
		_mp3Player.close();
		if (_actualAnswer == null) {
			SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						_gameBattle.disableGuess();							
					}
				}
			);
			setAnswer(null, System.nanoTime());
		}
	}
	
	public abstract void afterRound();
	

	public void setAnswer(ID3Summary answer, long answerTime) {
		_actualAnswer = answer;
		_actualAnswerTime = answerTime - _roundStartTime;
		afterSetAnswer();
	}

	public abstract void afterSetAnswer();
	
	protected abstract void interruptGame();
	
	public void quitGame() {
		this.interrupt();
	}

	
}