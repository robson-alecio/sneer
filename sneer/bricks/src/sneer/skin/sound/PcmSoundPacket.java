package sneer.skin.sound;

import sneer.pulp.tuples.Tuple;
import wheel.lang.ImmutableByteArray;

/** A packet of PCM-encoded sound: 8000Hz, 16 bits, 2 Channels (Stereo), Signed, Little Endian */
public class PcmSoundPacket extends Tuple {

	public final ImmutableByteArray _payload;

	public PcmSoundPacket(ImmutableByteArray payload) {
		_payload = payload;
	}
	
}
