package sneer.kernel.communication.impl;

import sneer.kernel.business.contacts.ContactId;

public class Packet {

	final String _channel;
	final Object _contents;

	public Packet(String channel, Object contents) {
		_channel = channel;
		_contents = contents;
	}

}
