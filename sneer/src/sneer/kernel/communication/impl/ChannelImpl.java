package sneer.kernel.communication.impl;

import java.util.LinkedList;
import java.util.List;

import sneer.kernel.communication.Channel;
import sneer.kernel.communication.Packet;
import wheel.io.Log;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.reactive.Signal;
import wheel.reactive.impl.SourceImpl;

class ChannelImpl implements Channel {

	private final SourceImpl<Packet> _input = new SourceImpl<Packet>(null);
	private final Omnivore<Packet> _output;
	
	private final List<Packet> _buffer = new LinkedList<Packet>();
	private final SourceImpl<Integer> _elementsInInputBuffer = new SourceImpl<Integer>(0);

	
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
			inputBufferChanged();
			_buffer.notify();
		}
	}

	private void inputBufferChanged() {
		_elementsInInputBuffer.setter().consume(_buffer.size());
	}

	private Packet waitForNextFromBuffer() {
		synchronized (_buffer) {
			if (_buffer.isEmpty())
				Threads.waitWithoutInterruptions(_buffer);
			
			//if (_buffer.size() > 1) System.out.println("Input buffer " + this + " size: " + _buffer.size());
			Packet result = _buffer.remove(0);
			inputBufferChanged();
			
			return result;
		}
	}

	@Override
	public Signal<Integer> elementsInInputBuffer() {
		return _elementsInInputBuffer.output();
	}

}
