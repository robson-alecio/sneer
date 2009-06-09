package snapps.whisper.speextuples;

import sneer.brickness.PublicKey;
import sneer.brickness.Tuple;
import sneer.hardware.ram.arrays.ImmutableByteArray2D;

public class SpeexPacket extends Tuple {
	
	public final ImmutableByteArray2D frames;
	public final String room;
	public final short sequence;

	public SpeexPacket(ImmutableByteArray2D frames_, String room_, short sequence_) {
		frames = frames_;
		room = room_;
		sequence = sequence_;
	}

	public SpeexPacket(PublicKey contactKey, ImmutableByteArray2D frames_, String room_, short sequence_) {
		super(contactKey, 0);
		frames = frames_;
		room = room_;
		sequence = sequence_;
	}
}