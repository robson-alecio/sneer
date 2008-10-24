package sneer.skin.sound.speaker.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import sneer.kernel.container.Inject;
import sneer.pulp.clock.Clock;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.tuples.TupleSpace;
import sneer.skin.sound.PcmSoundPacket;
import sneer.skin.sound.kernel.Audio;
import sneer.skin.sound.speaker.Speaker;
import wheel.lang.Omnivore;

public class SpeakerImpl implements Speaker, Omnivore<PcmSoundPacket> {
	
	private static final int FLUSH_INTERVAL = 300;

	@Inject
	private static TupleSpace _tupleSpace;
	
	@Inject
	private static KeyManager _keyManager;
	
	@Inject
	private static Audio _audio;
	
	@Inject
	private static Clock _clock;
	
	private List<PcmSoundPacket> _buffer = new ArrayList<PcmSoundPacket>();
	
	private SourceDataLine _line;

	private PcmSoundPacket _lastWritten = new PcmSoundPacket(null, 0, null, 0);
	
	{
		_clock.wakeUpEvery(FLUSH_INTERVAL, new Runnable() { @Override public void run() {
			flush();
		}});
	}

	@Override
	public void consume(PcmSoundPacket packet) {
		if (isMine(packet))
			return;
		
		if (isOlderThanLastPlayed(packet))
			return;
		
		write(packet);
	}
	
	@Override
	public void open() {
		if (!isClosed())
			return;
		
		initSourceDataLine();
		startListeningToPcmSoundPackets();
	}

	@Override
	public void close() {
		if (isClosed())
			return;
		
		stopListeningToPcmSoundPackets();
		closeSourceDataLine();
	}

	private void startListeningToPcmSoundPackets() {
		
		_tupleSpace.addSubscription(PcmSoundPacket.class, this);
	}

	private synchronized void initSourceDataLine() {
		_line = _audio.bestAvailableSourceDataLine();
		openSourceDataLine();
	}

	private void stopListeningToPcmSoundPackets() {
		_tupleSpace.removeSubscription(PcmSoundPacket.class, this);
	}

	private synchronized void closeSourceDataLine() {
		_line.close();
		_line = null;
	}

	private synchronized void ensureLineIsOpen() {
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
		synchronized (_buffer) {
			_buffer.add(packet);
		}
	}
	
	private void flush() {
		if (isClosed())
			return;
		
		ensureLineIsOpen();		
		
		synchronized (_buffer) {
			sortBuffer();
			playBuffer();
		}
	}

	private synchronized boolean isClosed() {
		return _line == null;
	}

	private synchronized void playBuffer() {
		final PcmSoundPacket first = _buffer.get(0);
		final Iterator<PcmSoundPacket> iterator = _buffer.iterator();
		while (iterator.hasNext()) {
			final PcmSoundPacket packet = iterator.next();
			if (packet.publicationTime() - first.publicationTime() > FLUSH_INTERVAL)
				break;
			play(packet);
			iterator.remove();
		}
	}

	private void sortBuffer() {
		Collections.sort(_buffer, new Comparator<PcmSoundPacket>() { @Override public int compare(PcmSoundPacket x, PcmSoundPacket y) {
			return x._sequence - y._sequence;
		}});
	}

	private void play(final PcmSoundPacket packet) {
		final byte[] buffer = packet._payload.copy();
		_line.write(buffer, 0, buffer.length);
		_lastWritten = packet;
	}
	
	protected boolean isOlderThanLastPlayed(PcmSoundPacket packet) {
		return packet.publicationTime() < _lastWritten.publicationTime();
	}

}
