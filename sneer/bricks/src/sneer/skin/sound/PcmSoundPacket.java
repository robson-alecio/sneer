package sneer.skin.sound;

import sneer.pulp.keymanager.PublicKey;
import sneer.pulp.tuples.Tuple;
import wheel.lang.ImmutableByteArray;

/** A packet of PCM-encoded sound: 8000Hz, 16 bits, 2 Channels (Stereo), Signed, Little Endian */
public class PcmSoundPacket extends Tuple {

	public static PcmSoundPacket newInstance(byte[] pcmBuffer, int read, int sequence) {
		return new PcmSoundPacket(new ImmutableByteArray(pcmBuffer, read), sequence);
	}

	public final ImmutableByteArray _payload;
	
	public final int _sequence;

	private PcmSoundPacket(ImmutableByteArray payload, int sequence) {
		_payload = payload;
		_sequence = sequence;
	}

	public PcmSoundPacket(PublicKey pPublisher, long pPublicationTime, ImmutableByteArray payload, int sequence) {
		super(pPublisher, pPublicationTime);
		_payload = payload;
		_sequence = sequence;
	}
}
