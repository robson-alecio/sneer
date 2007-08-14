package sneer.games.mediawars.mp3sushi;

import sneer.kernel.business.contacts.ContactAttributes;

public class TheirsGame extends Game {

	private final ContactAttributes _player;

	public TheirsGame(ContactAttributes player) {
		super();
		_player = player;
	}

	public ContactAttributes getPlayer() {
		return _player;
	}

	public boolean canReceiveJoins() {
		// Implement Auto-generated method stub
		return WAITING_PLAYERS.equals(_status.output().currentValue());
	}

}
