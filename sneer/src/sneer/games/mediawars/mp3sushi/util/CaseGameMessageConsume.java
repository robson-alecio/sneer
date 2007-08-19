package sneer.games.mediawars.mp3sushi.util;

import sneer.kernel.business.contacts.ContactId;

public interface CaseGameMessageConsume {

	public void consume(String type, ContactId contactiD, Object content);
	
}
