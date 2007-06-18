package sneer.kernel.communication.impl;

import sneer.kernel.business.contacts.ContactId;

public class MuxedPacket {

	final String _muxedConnectionId;
	final Object _contents;

	public MuxedPacket(String muxedConnectionId, Object contents) {
		_muxedConnectionId = muxedConnectionId;
		_contents = contents;
	}

}
