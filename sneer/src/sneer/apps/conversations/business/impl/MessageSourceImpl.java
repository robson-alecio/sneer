package sneer.apps.conversations.business.impl;

import sneer.apps.conversations.business.Message;
import sneer.apps.conversations.business.MessageSource;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.Source;
import wheel.reactive.impl.SourceImpl;

public class MessageSourceImpl implements MessageSource{

	public MessageSourceImpl(String author, String message, Long timestamp){ 
		_author = new SourceImpl<String>(author); 
		_message = new SourceImpl<String>(message); 
		_timestamp = new SourceImpl<Long>(timestamp); 
	}
	
	private final class MyOutput implements Message {

		public Signal<String> author() {
			return _author.output();
		}

		public Signal<String> message() {
			return _message.output();
		}

		public Signal<Long> timestamp() {
			return _timestamp.output();
		}
		
	}
	
	private MyOutput _output = new MyOutput();
	
	private Source<String> _author = new SourceImpl<String>("");
	private Source<String> _message = new SourceImpl<String>("");
	private Source<Long> _timestamp = new SourceImpl<Long>(null);

	public Message output() {
		return _output;
	}

	public Omnivore<String> authorSetter() {
		return _author.setter();
	}

	public Omnivore<String> messageSetter() {
		return _message.setter();
	}

	public Omnivore<Long> timestampSetter() {
		return _timestamp.setter();
	}

}
