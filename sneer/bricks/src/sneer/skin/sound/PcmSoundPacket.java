package sneer.skin.sound;

import sneer.brickness.PublicKey;
import sneer.brickness.Tuple;
import wheel.lang.ImmutableByteArray;

/** A packet of PCM-encoded sound: 8000Hz, 16 bits, 2 Channels (Stereo), Signed, Little Endian */
public class PcmSoundPacket extends Tuple {

	public final ImmutableByteArray payload;
	
	public PcmSoundPacket(ImmutableByteArray payload_) {
		payload = payload_;
	}

	public PcmSoundPacket(PublicKey publisher, long publicationTime, ImmutableByteArray payload_) {
		super(publisher, publicationTime);
		payload = payload_;
	}
}
