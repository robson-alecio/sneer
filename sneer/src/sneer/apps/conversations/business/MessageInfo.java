package sneer.apps.conversations.business;

import java.io.Serializable;

public class MessageInfo implements Serializable{

	public final String _author;
	public final String _message;
	public final Long _timestamp;
	
	public MessageInfo(String author, String message, Long timestamp){
		_author = author;
		_message = message;
		_timestamp = timestamp;
	}
	
	private static final long serialVersionUID = 1L;
}
