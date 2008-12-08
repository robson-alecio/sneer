package sneer.skin.sound.speaker.impl;

import sneer.skin.sound.speaker.Speaker;
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;

class SpeakerImpl implements Speaker {
	
	private PacketPlayer _consumer;
	private PacketSubscriber _producer;
	private Register<Boolean> _isRunning = new RegisterImpl<Boolean>(false);


	@Override
	synchronized public void open() {
		if (_producer != null) return;

		_consumer = new PacketPlayer();
		_producer = new PacketSubscriber(_consumer);
		_isRunning.setter().consume(true);
	}

	
	@Override
	synchronized public void close() {
		_isRunning.setter().consume(false);
		if (_producer == null) return;

		_producer.crash();
		_consumer.crash();

		_producer = null;
		_consumer = null;
	}


	@Override
	public Signal<Boolean> isRunning() {
		return _isRunning.output();
	}
}
