package sneer.bricks.skin.sound.speaker.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.skin.sound.speaker.Speaker;

class SpeakerImpl implements Speaker {
	
	private PacketPlayer _consumer;
	private PacketSubscriber _producer;
	private Register<Boolean> _isRunning = my(Signals.class).newRegister(false);


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
