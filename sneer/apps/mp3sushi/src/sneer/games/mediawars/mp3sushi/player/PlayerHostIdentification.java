package sneer.games.mediawars.mp3sushi.player;

import wheel.reactive.Signal;

public class PlayerHostIdentification extends PlayerIdentification {

	private final Long _id = new Long(0);
	private Signal<String> _ownName; 

	public PlayerHostIdentification(Signal<String> ownName) {
		_ownName = ownName;
	}

	@Override
	public Long getId() {
		return _id;
	}

	@Override
	public Signal<String> getNick() {
		// Implement Auto-generated method stub
		return _ownName;
	}
	
	public PlayerExportInfo exportInfo() {
		return new PlayerExportInfo(_id, _ownName.currentValue());
	}


}
