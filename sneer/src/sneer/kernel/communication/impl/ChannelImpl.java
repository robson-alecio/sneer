package sneer.kernel.communication.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.Connection;
import wheel.lang.exceptions.NotImplementedYet;

class ChannelImpl implements Channel {

	private final String _channelId;

	ChannelImpl(String channelId) {
		_channelId = channelId;
	}

	@Override
	public Connection connectionTo(ContactId contactId) {
		throw new NotImplementedYet();
	}

}
