package sneer.kernel.communication.impl;

import sneer.kernel.communication.Packet;

public class ChannelPacket {

	public final String _channelId;
	public final Packet _packet;

	public ChannelPacket(String channelId, Packet packet) {
		_channelId = channelId;
		_packet = packet;
	}

}
