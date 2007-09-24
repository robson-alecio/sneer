package sneer.kernel.communication.impl;

import java.io.NotSerializableException;
import java.util.LinkedList;
import java.util.List;

import sneer.kernel.communication.Channel;
import sneer.kernel.communication.Packet;
import wheel.io.Log;
import wheel.io.serialization.DefaultDeserializer;
import wheel.io.serialization.DefaultSerializer;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.reactive.Signal;
import wheel.reactive.impl.SourceImpl;

class ChannelImpl implements Channel {

	private final SourceImpl<Packet> _input = new SourceImpl<Packet>(null);
	private final Omnivore<Packet> _output;
	private final Omnivore<Packet> _outputSerializer;
	
	private final List<Packet> _buffer = new LinkedList<Packet>();
	private final SourceImpl<Integer> _elementsInInputBuffer = new SourceImpl<Integer>(0);

	private final DefaultSerializer _serializer = new DefaultSerializer();
	private final DefaultDeserializer _deserializer;
	
	ChannelImpl(Omnivore<Packet> output, ClassLoader classLoader) {
		_deserializer = new DefaultDeserializer(classLoader);
		_output = output;
		_outputSerializer = createOutputSerializer();
		startConsumer();
	}

	private Omnivore<Packet> createOutputSerializer() {
		return new Omnivore<Packet>() { public void consume(Packet packet) {
			byte[] serializedContents;
			try {
				serializedContents = _serializer.serialize(packet._contents);
			} catch (NotSerializableException e) {
				Log.log(e);
				return;
			}
			Packet classLoadable = new Packet(packet._contactId, serializedContents); 
			_output.consume(classLoadable);
		}}; 
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
		return _outputSerializer;
	}

	void receive(Packet classLoadable) throws ClassNotFoundException {
		Object contents = _deserializer.deserialize((byte[])classLoadable._contents);
		Packet packet = new Packet(classLoadable._contactId, contents);
		
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
