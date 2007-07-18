package sneer.kernel.communication.impl;

import java.util.LinkedList;
import java.util.List;

import sneer.kernel.communication.Channel;
import sneer.kernel.communication.Packet;
import wheel.io.Log;
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
				Packet packet = waitForNextFromBuffer();
				try {
					_input.setter().consume(packet);
				} catch (RuntimeException e) {
					Log.log(e);
				} 
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

	private Packet waitForNextFromBuffer() {
		synchronized (_buffer) {
			if (_buffer.isEmpty())
				Threads.waitWithoutInterruptions(_buffer);
			if (_buffer.size() > 1) System.out.println("Input buffer " + this + " size: " + _buffer.size());
			return _buffer.remove(0);
		}
	}

}
