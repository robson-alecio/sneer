package sneer.kernel.business.contacts.impl;

import java.io.Serializable;

import sneer.kernel.business.contacts.Contact;
import sneer.kernel.business.contacts.ContactSource;
import wheel.io.network.PortNumberSource;
import wheel.lang.Consumer;
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

	}


	public ContactSourceImpl(String nick, String host, int port) {
		_nick = new SourceImpl<String>(nick);
		_host = new SourceImpl<String>(host);
		_port = new PortNumberSource(port);
	}

	private final Contact _output = new MyOutput();
	
	private final Source<String> _nick;
	private final Source<String> _host;
	private final PortNumberSource _port;
	private final Source<Boolean> _isOnline = new SourceImpl<Boolean>(false);  //Optimize: Do not store online events in the transaction log. Make this transient or remove it from the business logic.


	@Override
	public Contact output() {
		return _output;
	}


	@Override
	public Consumer<String> nickSetter() {
		return _nick.setter();
	}

	
	@Override
	public Consumer<String> hostSetter() {
		return _host.setter();
	}

	
	@Override
	public Consumer<Integer> portSetter() {
		return _port.setter();
	}


	@Override
	public Consumer<Boolean> isOnlineSetter() {
		return _isOnline.setter();
	}

}
