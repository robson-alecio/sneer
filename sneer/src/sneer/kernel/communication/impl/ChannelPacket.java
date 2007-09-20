package sneer.kernel.communication.impl;

import java.io.Serializable;

import sneer.kernel.communication.Packet;

public class ChannelPacket implements Serializable {

	public final String _channelId;
	public final Packet _packet;

	public ChannelPacket(String channelId, Packet packet) {
		_channelId = channelId;
		_packet = packet;
	}

	private static final long serialVersionUID = 1L;

}
