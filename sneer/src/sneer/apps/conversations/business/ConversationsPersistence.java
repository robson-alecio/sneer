package sneer.apps.conversations.business;

import java.awt.Rectangle;

import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;

public interface ConversationsPersistence {
	
	Signal<Rectangle> bounds();
	
	ListSignal<Message> messages();
	
}
