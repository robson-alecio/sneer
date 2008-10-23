package sneer.skin.sound.speaker.impl;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import sneer.kernel.container.Inject;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.tuples.TupleSpace;
import sneer.skin.sound.PcmSoundPacket;
import sneer.skin.sound.kernel.Audio;
import sneer.skin.sound.speaker.Speaker;
import wheel.lang.Omnivore;

public class SpeakerImpl implements Speaker, Omnivore<PcmSoundPacket> {
	
	@Inject
	private static TupleSpace _tupleSpace;
	
	@Inject
	private static KeyManager _keyManager;
	
	@Inject
	private static Audio _audio;
	
	private SourceDataLine _line;

	private PcmSoundPacket _lastWritten = new PcmSoundPacket(null, 0, null, 0);

	@Override
	public void consume(PcmSoundPacket packet) {
		if (isMine(packet))
			return;
		
		if (isOlderThanLast(packet))
			return;
		
		ensureLineIsOpen();		
		write(packet);
	}
	
	@Override
	public void open() {
		initSourceDataLine();
		startListeningToPcmSoundPackets();
	}

	@Override
	public void close() {
		if (_line == null)
			return;
		
		stopListeningToPcmSoundPackets();
		closeSourceDataLine();
	}

	private void startListeningToPcmSoundPackets() {
		
		_tupleSpace.addSubscription(PcmSoundPacket.class, this);
	}

	private void initSourceDataLine() {
		_line = _audio.bestAvailableSourceDataLine();
		openSourceDataLine();
	}

	private void stopListeningToPcmSoundPackets() {
		_tupleSpace.removeSubscription(PcmSoundPacket.class, this);
	}

	private void closeSourceDataLine() {
		_line.close();
		_line = null;
	}

	private void ensureLineIsOpen() {
		if (!_line.isActive()) {
			openSourceDataLine();
		}
	}

	private void openSourceDataLine() {
		try {
			_line.open();
		} catch (LineUnavailableException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
		_line.start();
	}

	private boolean isMine(PcmSoundPacket packet) {
		return _keyManager.ownPublicKey().equals(packet.publisher());
	}

	private void write(PcmSoundPacket packet) {
		_lastWritten = packet;
		final byte[] buffer = packet._payload.copy();
		_line.write(buffer, 0, buffer.length);
	}
	
	protected boolean isOlderThanLast(PcmSoundPacket packet) {
		return packet.publicationTime() < _lastWritten.publicationTime();
	}

}
