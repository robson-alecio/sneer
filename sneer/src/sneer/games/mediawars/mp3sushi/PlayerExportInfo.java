package sneer.games.mediawars.mp3sushi;

public class PlayerExportInfo {

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
	
	
}
