package sneer.kernel.business.contacts.impl;

import sneer.kernel.business.contacts.ContactAttributes;
import sneer.kernel.business.contacts.ContactAttributesSource;
import sneer.kernel.business.contacts.ContactId;
import wheel.io.network.PortNumberSource;
import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.Source;
import wheel.reactive.impl.SourceImpl;

public class ContactAttributesSourceImpl implements ContactAttributesSource {


	public class MyOutput implements ContactAttributes {

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

		public ContactId id() {
			return new ContactIdImpl(_id);
		}

		public Signal<String> publicKey() {
			return _publicKey.output();
		}

		@Override
		public Signal<Boolean> publicKeyConfirmed() {
			return new SourceImpl<Boolean>(false).output(); //Implement
		}

	}


	public ContactAttributesSourceImpl(String nick, String host, int port, String publicKey, long id) {
		nick.toString();
		_nick = new SourceImpl<String>(nick);
		_host = new SourceImpl<String>(host);
		_port = new PortNumberSource(port);
		_publicKey = new SourceImpl<String>(publicKey);
		_id = id;
	}

	private final ContactAttributes _output = new MyOutput();
	
	private final Source<String> _nick;
	private final Source<String> _host;
	private final PortNumberSource _port;

	private final Source<String> _publicKey;
	
	
	private final long _id;

	@Override
	public ContactAttributes output() {
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
	public Omnivore<String> publicKeySetter() {
		return _publicKey.setter();
	}

}
