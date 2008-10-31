package sneer.skin.sound.speaker.impl;

import sneer.kernel.container.Inject;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.tuples.TupleSpace;
import sneer.skin.sound.PcmSoundPacket;
import wheel.lang.Omnivore;

class PacketSubscriber implements Omnivore<PcmSoundPacket> {

	@Inject private static TupleSpace _tupleSpace;
	@Inject private static KeyManager _keyManager;
	
	
	public PacketSubscriber(Omnivore<PcmSoundPacket> consumer) {
		_consumer = consumer;
		_tupleSpace.addSubscription(PcmSoundPacket.class, this);
	}
	
	
	private boolean _isRunning = true;

	private Omnivore<PcmSoundPacket> _consumer;

	
	synchronized void crash() {
		_isRunning = false;
		_tupleSpace.removeSubscription(this);
	}
	
	
	@Override
	synchronized public void consume(PcmSoundPacket packet) {
		if (!_isRunning) return;
		if (isMine(packet))	return;

		_consumer.consume(packet);
	}

	
	private boolean isMine(PcmSoundPacket packet) {
		return _keyManager.ownPublicKey().equals(packet.publisher());
	}

	
}
