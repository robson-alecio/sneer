package sneer.apps.conversations.business.impl;

import java.awt.Rectangle;

import sneer.apps.conversations.business.ConversationsPersistence;
import sneer.apps.conversations.business.ConversationsPersistenceSource;
import sneer.apps.conversations.business.Message;
import sneer.apps.conversations.business.MessageInfo;
import sneer.apps.conversations.business.MessageSource;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.Source;
import wheel.reactive.impl.SourceImpl;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListSource;
import wheel.reactive.lists.impl.ListSourceImpl;

public class ConversationsPersistenceSourceImpl implements ConversationsPersistenceSource{

	private final class MyOutput implements ConversationsPersistence {

		public ListSignal<Message> messages() {
			return _messages.output(); 
		}

		public Signal<Rectangle> bounds() {
			return _bounds.output();
		}
		
	}
	
	private MyOutput _output = new MyOutput();

	private final Source<Rectangle> _bounds = new SourceImpl<Rectangle>(new Rectangle(0,0,400,300));
	
	private final ListSource<Message> _messages = new ListSourceImpl<Message>(); 
	private final ListSource<MessageSource> _messagesSources = new ListSourceImpl<MessageSource>(); 
	
	public ConversationsPersistence output() {
		return _output;
	}

	//Fix: Can't use this directly because Bubble only wraps top class. :(
	public Omnivore<MessageInfo> archive() {
		return new Omnivore<MessageInfo>(){ public void consume(MessageInfo message) {
			MessageSource tempSource = new MessageSourceImpl(message._author, message._message, message._timestamp);
			_messagesSources.add(tempSource);
			_messages.add(tempSource.output());
		}};
	}

	//Fix: Can't use this directly because Bubble only wraps top class. :(
	public Omnivore<Rectangle> boundsSetter() {
		return _bounds.setter();
	}

}
