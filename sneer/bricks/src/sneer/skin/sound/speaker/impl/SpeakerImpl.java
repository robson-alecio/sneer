package sneer.skin.sound.speaker.impl;

import sneer.kernel.container.Inject;
import sneer.skin.sound.speaker.Speaker;
import sneer.skin.sound.speaker.buffer.SpeakerBuffer;
import sneer.skin.sound.speaker.buffer.SpeakerBuffers;

class SpeakerImpl implements Speaker {
	
	@Inject
	static private SpeakerBuffers _buffers;
	
	private PacketSubscriber _producer;
	
	private PacketPlayer _consumer;

	private SpeakerBuffer _buffer;

	
	@Override
	synchronized public void open() {
		if (_producer != null) return;

		_consumer = new PacketPlayer();
		_buffer = _buffers.createBufferFor(_consumer);
		_producer = new PacketSubscriber(_buffer);
	}

	
	@Override
	synchronized public void close() {
		if (_producer == null) return;

		_producer.crash();
		_consumer.crash();
		_buffer.crash();

		_producer = null;
		_consumer = null;
		_buffer = null;
	}

}
