package sneer.skin.sound;

import sneer.pulp.keymanager.PublicKey;
import sneer.pulp.tuples.Tuple;
import wheel.lang.ImmutableByteArray;

/** A packet of PCM-encoded sound: 8000Hz, 16 bits, 2 Channels (Stereo), Signed, Little Endian */
public class PcmSoundPacket extends Tuple {

	public static PcmSoundPacket newInstance(byte[] pcmBuffer, int read, short sequence) {
		return new PcmSoundPacket(new ImmutableByteArray(pcmBuffer, read), sequence);
	}

	public final ImmutableByteArray payload;
	
	public final short sequence;

	private PcmSoundPacket(ImmutableByteArray payload_, short sequence_) {
		payload = payload_;
		sequence = sequence_;
	}

	public PcmSoundPacket(PublicKey publisher, long publicationTime, ImmutableByteArray payload_, short sequence_) {
		super(publisher, publicationTime);
		payload = payload_;
		sequence = sequence_;
	}
	
	@Override
	public String toString() {
		return "PcmSoundPacket(" + sequence + ")";
	}
}
