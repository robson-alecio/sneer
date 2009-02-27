package sneer.skin.sound.speaker.impl;

import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.tuples.TupleSpace;
import sneer.skin.sound.PcmSoundPacket;
import wheel.lang.Consumer;
import static sneer.brickness.Environments.my;

class PacketSubscriber implements Consumer<PcmSoundPacket> {

	private final TupleSpace _tupleSpace = my(TupleSpace.class);
	private final KeyManager _keyManager = my(KeyManager.class);
	
	private boolean _isRunning = true;
	private Consumer<PcmSoundPacket> _consumer;

	public PacketSubscriber(Consumer<PcmSoundPacket> consumer) {
		_consumer = consumer;
		_tupleSpace.addSubscription(PcmSoundPacket.class, this);
	}
	
	synchronized void crash() {
		_isRunning = false;
		_tupleSpace.removeSubscriptionAsync(this);
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