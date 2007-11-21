package sneer.apps.conversations.business;

import java.awt.Rectangle;

import wheel.lang.Omnivore;

public interface ConversationsPersistenceSource {

	ConversationsPersistence output();
	
	Omnivore<Rectangle> boundsSetter();
	
	Omnivore<MessageInfo> archive();

}
