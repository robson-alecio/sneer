package sneer.games.mediawars.mp3sushi;

import wheel.reactive.Signal;
import wheel.reactive.Source;
import wheel.reactive.impl.SourceImpl;

public class PlayerExternalIdentification extends PlayerIdentification {
	
	private final Long _id;
	private final Source<String> _nick;
	
	public PlayerExternalIdentification(PlayerExportInfo pei) {
		_id = pei.getId();
		_nick = new SourceImpl<String>(pei.getNick());
	}

	@Override
	public Signal<String> getNick() {
		return _nick.output();
	}
	
	@Override
	public Long getId() {
		return _id;
	}
	
	public PlayerExportInfo exportInfo() {
		return new PlayerExportInfo(_id, _nick.output().currentValue());
	}



}
