package sneer.kernel.communication.impl;

import sneer.kernel.communication.Channel;
import sneer.kernel.communication.Packet;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.SourceImpl;

class ChannelImpl implements Channel {

	private final SourceImpl<Packet> _input = new SourceImpl<Packet>(null);
	private final Omnivore<Packet> _output;

	ChannelImpl(Omnivore<Packet> output) {
		_output = output;
	}

	public Signal<Packet> input() {
		return _input.output();
	}

	public Omnivore<Packet> output() {
		return _output;
	}

	void receive(Packet packet) {
		_input.setter().consume(packet);		
	}

}
