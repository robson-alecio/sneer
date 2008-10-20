package sneer.skin.sound.speaker.impl;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import sneer.kernel.container.Inject;
import sneer.pulp.tuples.TupleSpace;
import sneer.skin.sound.PcmSoundPacket;
import sneer.skin.sound.kernel.Audio;
import sneer.skin.sound.speaker.Speaker;
import wheel.lang.Omnivore;

public class SpeakerImpl implements Speaker {
	
	@Inject
	private static TupleSpace _tupleSpace;
	
	@Inject
	private static Audio _audio;
	
	private SourceDataLine _line;

	private final Omnivore<PcmSoundPacket> _pcmSoundPacketConsumer = new Omnivore<PcmSoundPacket>(){ @Override public void consume(PcmSoundPacket packet) {
		final byte[] buffer = packet._payload.copy();
		_line.write(buffer, 0, buffer.length);
	}};
	
	@Override
	public void open() {
		try {
			openSourceDataLine();
		} catch (LineUnavailableException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
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
		
		_tupleSpace.addSubscription(PcmSoundPacket.class, _pcmSoundPacketConsumer);
	}

	private void openSourceDataLine() throws LineUnavailableException {
		_line = _audio.bestAvailableSourceDataLine();
		_line.open();
		_line.start();
	}

	private void stopListeningToPcmSoundPackets() {
		_tupleSpace.removeSubscription(PcmSoundPacket.class, _pcmSoundPacketConsumer);
	}

	private void closeSourceDataLine() {
		_line.close();
		_line = null;
	}
}
