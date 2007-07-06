package sneer.kernel.communication.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.Packet;
import wheel.io.Connection;
import wheel.lang.Omnivore;
import wheel.lang.exceptions.NotImplementedYet;
import wheel.reactive.Signal;

class ChannelImpl implements Channel {

	interface MuxProvider {
		Mux muxFor(ContactId contactId);
	}
	
	private final String _channelId;
	private final MuxProvider _muxProvider;

	ChannelImpl(String channelId, MuxProvider muxProvider) {
		_channelId = channelId;
		_muxProvider = muxProvider;
	}

//	@Override
//	public Connection connectionTo(ContactId contactId) {
//		return muxFor(contactId).muxedConnectionFor(_channelId);
//	}

	private Mux muxFor(ContactId contactId) {
		return _muxProvider.muxFor(contactId);
	}

	public Signal<ContactId> lastContactRequestingConnection() {
		throw new NotImplementedYet();
	}

	public Signal<Packet> input() {
		// Implement Auto-generated method stub
		return null;
	}

	public Omnivore<Packet> output() {
		// Implement Auto-generated method stub
		return null;
	}

}
