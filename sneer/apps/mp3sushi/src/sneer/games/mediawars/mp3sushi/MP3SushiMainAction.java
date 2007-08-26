package sneer.games.mediawars.mp3sushi;

import wheel.io.ui.Action;

public class MP3SushiMainAction implements Action{

	private final MP3SushiGameApp _game;
	public MP3SushiMainAction(MP3SushiGameApp game){
		_game = game;
	}
	
	public String caption() {
		return "MP3 Sushi";
	}

	public void run() {
		_game.start();
	}

}
