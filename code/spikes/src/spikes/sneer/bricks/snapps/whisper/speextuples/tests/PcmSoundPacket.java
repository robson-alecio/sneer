package spikes.sneer.bricks.snapps.whisper.speextuples.tests;

import sneer.bricks.hardware.ram.arrays.ImmutableByteArray;
import sneer.foundation.brickness.Seal;
import sneer.foundation.brickness.Tuple;

//This class was deleted. It is here just to make the test compile.
class PcmSoundPacket extends Tuple {

	public final ImmutableByteArray payload;
	
	public PcmSoundPacket(ImmutableByteArray payload_) {
		payload = payload_;
	}

	public PcmSoundPacket(Seal publisher, long publicationTime, ImmutableByteArray payload_) {
		super(publisher, publicationTime);
		payload = payload_;
	}
}
