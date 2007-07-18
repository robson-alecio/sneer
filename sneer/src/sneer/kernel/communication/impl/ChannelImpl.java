package sneer.kernel.communication.impl;

import java.util.LinkedList;
import java.util.List;

import sneer.kernel.communication.Channel;
import sneer.kernel.communication.Packet;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.reactive.Signal;
import wheel.reactive.SourceImpl;

class ChannelImpl implements Channel {

	private final SourceImpl<Packet> _input = new SourceImpl<Packet>(null);
	private final Omnivore<Packet> _output;
	private final List<Packet> _buffer = new LinkedList<Packet>();

	ChannelImpl(Omnivore<Packet> output) {
		_output = output;
		startConsumer();
	}

	private void startConsumer() { 
		Threads.startDaemon(new Runnable() { public void run() { 
			while (true) {
				Packet packet;
				synchronized (_buffer) {
					if (_buffer.isEmpty())
						Threads.waitWithoutInterruptions(_buffer);
					packet = _buffer.remove(0);
				}
				_input.setter().consume(packet); 
			} 
		}}); 
	} 
	
	public Signal<Packet> input() {
		return _input.output();
	}

	public Omnivore<Packet> output() {
		return _output;
	}

	void receive(Packet packet) {
		synchronized (_buffer) {
			_buffer.add(packet);
			_buffer.notify();
		}
	}

}
