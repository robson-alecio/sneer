package sneer.games.mediawars.mp3sushi;

public class GameMessage {

	public static final String STATUS_REPORT = "Status_Report";
	public static final String CONFIGURATION_REPORT = "Configuration_Report";	
	public static final String STATUS_REQUEST = "Status_Request";
	public static final String JOIN_REQUEST = "Join_Request";	
	public static final String JOIN_ACCEPTED = "Join_Accepted";	
	public static final String JOIN_NOT_ACCEPTED = "Join_Not_Accepted";
	public static final String JOINED = "Joined";
	public static final String UNJOINED = "Unjoined";
	public static final String MP3S_DOMAIN = "Mp3s Domain";
	public static final String NEXT_MP3 = "Next Mp3";
	public static final String PLAY_ROUND = "Play Round";
	public static final String ANSWER = "Answer";
	public static final String ANSWER_SCORE = "Answer Score";
	public  static final String RECEIVE_MP3 = "Receive MP3";
	public  static final String HOST_QUIT = "Host quit";
	public  static final String PLAYER_QUIT = "Player quit";
	
	private String _type;
	private Object _content;
	
	public GameMessage(String type, Object content) {
		super();
		_type = type;
		_content = content;
	}

	public String getType() {
		return _type;
	}

	public Object getContent() {
		return _content;
	} 
	
}
