package sneer.bricks.pulp.internetaddresskeeper.impl;

import sneer.bricks.network.social.Contact;
import sneer.bricks.pulp.internetaddresskeeper.InternetAddress;

class InternetAddressImpl implements InternetAddress {

	private Contact _contact;
	
	private String _host;
	
	private int _port;
	
	public InternetAddressImpl(Contact contact, String host, int port) {
		_contact = contact;
		_host = host;
		_port = port;
	}

	@Override
	public Contact contact() {
		return _contact;
	}

	@Override
	public String host() {
		return _host;
	}

	@Override
	public int port() {
		return _port;
	}

	@Override
	public String toString() {
		return _contact+" ("+_host+" : "+_port+")";
	}
}
