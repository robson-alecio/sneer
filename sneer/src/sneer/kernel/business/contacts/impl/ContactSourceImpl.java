package sneer.kernel.business.contacts.impl;

import sneer.kernel.business.contacts.Contact;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.business.contacts.ContactSource;
import wheel.io.network.PortNumberSource;
import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.Source;
import wheel.reactive.SourceImpl;

public class ContactSourceImpl implements ContactSource {
	
	public class MyOutput implements Contact {

		@Override
		public Signal<String> host() {
			return _host.output();
		}

		@Override
		public Signal<String> nick() {
			return _nick.output();
		}

		@Override
		public Signal<Integer> port() {
			return _port.output();
		}

		@Override
		public Signal<Boolean> isOnline() {
			return _isOnline.output();
		}

		public ContactId id() {
			return new ContactIdImpl(_id);
		}

	}


	public ContactSourceImpl(String nick, String host, int port, long id) {
		_nick = new SourceImpl<String>(nick);
		_host = new SourceImpl<String>(host);
		_port = new PortNumberSource(port);
		_id = id;
	}

	private final Contact _output = new MyOutput();
	
	private final Source<String> _nick;
	private final Source<String> _host;
	private final PortNumberSource _port;
	
	private final Source<Boolean> _isOnline = new SourceImpl<Boolean>(false);  //Optimize: Do not store online events in the transaction log. Make this transient or remove it from the business logic.
	
	private final long _id;




	@Override
	public Contact output() {
		return _output;
	}


	@Override
	public Omnivore<String> nickSetter() {
		return _nick.setter();
	}

	
	@Override
	public Omnivore<String> hostSetter() {
		return _host.setter();
	}

	
	@Override
	public Consumer<Integer> portSetter() {
		return _port.setter();
	}


	@Override
	public Omnivore<Boolean> isOnlineSetter() {
		return _isOnline.setter();
	}

}
