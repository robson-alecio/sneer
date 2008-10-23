package sneer.skin.sound;

import java.util.concurrent.atomic.AtomicInteger;

import sneer.pulp.keymanager.PublicKey;
import sneer.pulp.tuples.Tuple;
import wheel.lang.ImmutableByteArray;

/** A packet of PCM-encoded sound: 8000Hz, 16 bits, 2 Channels (Stereo), Signed, Little Endian */
public class PcmSoundPacket extends Tuple {

	public static PcmSoundPacket newInstance(byte[] pcmBuffer, int read) {
		return new PcmSoundPacket(new ImmutableByteArray(pcmBuffer, read));
	}

	public final ImmutableByteArray _payload;
	
	public final int _sequence = nextInt();

	private PcmSoundPacket(ImmutableByteArray payload) {
		_payload = payload;
	}

	public PcmSoundPacket(PublicKey pPublisher, long pPublicationTime, ImmutableByteArray payload) {
		super(pPublisher, pPublicationTime);
		_payload = payload;
	}

	private static final AtomicInteger _ids = new AtomicInteger();

	private static int nextInt() {
		return _ids.incrementAndGet();
	}	
}
