package sneer.skin.sound.speaker.impl;

import sneer.skin.sound.speaker.Speaker;

class SpeakerImpl implements Speaker {
	
	private PacketSubscriber _producer;
	
	private PacketPlayer _consumer;

	@Override
	synchronized public void open() {
		if (_producer != null) return;

		_consumer = new PacketPlayer();
		_producer = new PacketSubscriber(_consumer);
	}

	
	@Override
	synchronized public void close() {
		if (_producer == null) return;

		_producer.crash();
		_consumer.crash();

		_producer = null;
		_consumer = null;
	}
}
