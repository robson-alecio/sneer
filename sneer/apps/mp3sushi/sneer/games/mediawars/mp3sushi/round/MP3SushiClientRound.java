package sneer.games.mediawars.mp3sushi.round;

import sneer.games.mediawars.mp3sushi.GameMessage;
import sneer.games.mediawars.mp3sushi.ID3Answer;
import sneer.games.mediawars.mp3sushi.MP3SushiGameApp;
import sneer.games.mediawars.mp3sushi.MyGame;
import sneer.games.mediawars.mp3sushi.gui.MP3SushiGameBattleFrame;

public class MP3SushiClientRound extends MP3SushiRound {
	
	private Object _roundStartLock = new Object();
	private boolean _roundStarted = false;
	
	public MP3SushiClientRound(MP3SushiGameApp mp3SushiGameApp,
			MP3SushiGameBattleFrame gameBattle, MyGame myGame) {
		super(mp3SushiGameApp, gameBattle, myGame);
		// Implement Auto-generated constructor stub
	}

	@Override
	public void preRound() throws InterruptedException {
		synchronized(_roundStartLock) {
			while(!_roundStarted) _roundStartLock.wait();
		}
	}
	
	@Override
	public void afterSetAnswer() {
		_roundStarted = false;
		_mp3SushiGameApp.sendGameMessage(_myGame.getExternalHostId(), GameMessage.ANSWER, new ID3Answer(_actualAnswer, _actualAnswerTime));
	}

	@Override
	public void afterRound() {
	}

	public void receiveNextMP3(byte[] nextMP3) {
		_nextMp3s = nextMP3;
		_mp3SushiGameApp.sendGameMessage(_myGame.getExternalHostId(), GameMessage.RECEIVE_MP3, null);
	}

	public void roundStart() {
		synchronized(_roundStartLock) {
			_roundStarted = true;
			 _roundStartLock.notify();
		}
	}
	
	@Override
	protected void interruptGame() {
		_mp3SushiGameApp.sendGameMessage(_myGame.getExternalHostId(), GameMessage.PLAYER_QUIT, null);
	}
}
