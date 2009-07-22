package spikes.bamboo.mocotoh;

import sneer.bricks.hardware.ram.arrays.ImmutableByteArray;
import sneer.foundation.brickness.Seal;
import sneer.foundation.brickness.Tuple;

/** A packet of PCM-encoded sound: 8000Hz, 16 bits, 2 Channels (Stereo), Signed, Little Endian */
public class PcmSoundPacket extends Tuple {

	public final ImmutableByteArray payload;
	
	public PcmSoundPacket(ImmutableByteArray payload_) {
		payload = payload_;
	}

	public PcmSoundPacket(Seal publisher, long publicationTime, ImmutableByteArray payload_) {
		super(publisher, publicationTime);
		payload = payload_;
	}
}
