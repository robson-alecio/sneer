package sneer.skin.sound;

import sneer.pulp.keymanager.PublicKey;
import sneer.pulp.tuples.Tuple;
import wheel.lang.ImmutableByteArray;

/** A packet of PCM-encoded sound: 8000Hz, 16 bits, 2 Channels (Stereo), Signed, Little Endian */
public class PcmSoundPacket extends Tuple {

	public final ImmutableByteArray payload;
	public final int channel;
	
	public PcmSoundPacket(ImmutableByteArray payload_, int channel_) {
		payload = payload_;
		channel = channel_;
	}

	public PcmSoundPacket(PublicKey publisher, long publicationTime, ImmutableByteArray payload_, int channel_) {
		super(publisher, publicationTime);
		payload = payload_;
		channel = channel_;
	}
}
