package sneer.games.mediawars.mp3sushi.round;

import java.util.Set;
import java.util.Map.Entry;

import sneer.games.mediawars.mp3sushi.GameConfiguration;
import sneer.games.mediawars.mp3sushi.GameMessage;
import sneer.games.mediawars.mp3sushi.ID3Summary;
import sneer.games.mediawars.mp3sushi.MP3SushiGameApp;
import sneer.games.mediawars.mp3sushi.MyGame;
import sneer.games.mediawars.mp3sushi.gui.MP3SushiGameBattleFrame;
import sneer.games.mediawars.mp3sushi.player.PlayerContactIdentification;
import sneer.games.mediawars.mp3sushi.player.PlayerIdentification;
import sneer.kernel.business.contacts.ContactId;


public class MP3SushiHostRound extends MP3SushiRound {
	
	Mp3PieceProvider _mp3SushiRoundProvider;
	private Object _lockAnswer = new Object();
	private Object _lockMp3sReceive = new Object();
	int _answers = 0;
	private int _mp3sReceive = 0;
	public MP3SushiHostRound(MP3SushiGameApp mp3SushiGameApp,
			MP3SushiGameBattleFrame gameBattle, MyGame myGame,Mp3PieceProvider mp3SushiRoundProvider) {
		super(mp3SushiGameApp, gameBattle, myGame);
		_mp3SushiRoundProvider = mp3SushiRoundProvider;
	}

	@Override
	public void preRound() throws InterruptedException {
		
		_nextMp3s = _mp3SushiRoundProvider.nextMP3Piece();
		_answers = 0;
		_mp3sReceive = 0;
		Set<Entry<ContactId, PlayerContactIdentification>> entrySet = _myGame.getContactPlayer().entrySet();
		for ( Entry<ContactId, PlayerContactIdentification> player : entrySet ) {
			_mp3SushiGameApp.sendGameMessage(player.getKey(), GameMessage.NEXT_MP3, _nextMp3s);
		}
		synchronized (_lockMp3sReceive) {
			while (_mp3sReceive < entrySet.size() ) {_lockMp3sReceive.wait();}	
		}
		
		for ( Entry<ContactId, PlayerContactIdentification> player : entrySet ) {
			_mp3SushiGameApp.sendGameMessage(player.getKey(), GameMessage.PLAY_ROUND, null);
		}
	}
	
	@Override
	public void afterSetAnswer() {
		receiveAnswer(_myGame.getHostPlayer(), _actualAnswer, _actualAnswerTime);
	}
	
	public void receiveAnswer(PlayerIdentification playerId, ID3Summary answer, long time) {
		GameConfiguration gc = _myGame.getGameConfiguration().output().currentValue();
		int score = 0;
		if (_mp3SushiRoundProvider.isGoodAnswer(answer)) {
			int timeInSeconds = (int) (time / 1000000000);
			score = gc.getSecondsOfMusic() + gc.getSecondsToGuess() - timeInSeconds;
			playerId.setActualScore(score);
		}
		for ( Entry<ContactId, PlayerContactIdentification> player : _myGame.getContactPlayer().entrySet() ) {
			_mp3SushiGameApp.sendGameMessage(player.getKey(), GameMessage.ANSWER_SCORE, new AnswerScore(playerId.getId(), score));
		}
		synchronized (_lockAnswer) {
			_answers++;
			_lockAnswer.notify();
		}
	}

	public void receiveMP3() {
		synchronized (_lockMp3sReceive) {
			_mp3sReceive++;
			_lockMp3sReceive.notify();
		}
	}

	@Override
	public void afterRound() {
		synchronized (_lockAnswer) {
			while (_answers < _myGame.getPlayers().currentSize()) {
				try {
					_lockAnswer.wait();
				} catch (InterruptedException e) {
				}
			}	
		}
	}

	@Override
	protected void interruptGame() {
		for ( Entry<ContactId, PlayerContactIdentification> player : _myGame.getContactPlayer().entrySet() ) {
			_mp3SushiGameApp.sendGameMessage(player.getKey(), GameMessage.HOST_QUIT, null);
		}

	}

	
}
