package sneer.games.mediawars.mp3sushi.player;

import java.io.Serializable;

public class PlayerExportInfo implements Serializable{

	private long _id;
	private String _nick;
	
	public long getId() {
		return _id;
	}
	
	public String getNick() {
		return _nick;
	}
	
	public PlayerExportInfo(long id, String nick) {
		super();
		_id = id;
		_nick = nick;
	}
	
	private static final long serialVersionUID = 1L;
	
}
