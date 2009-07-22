package spikes.sneer.bricks.snapps.whisper.speextuples;

import sneer.bricks.hardware.ram.arrays.ImmutableByteArray2D;
import sneer.foundation.brickness.Seal;
import sneer.foundation.brickness.Tuple;

public class SpeexPacket extends Tuple {
	
	public final ImmutableByteArray2D frames;
	public final String room;
	public final short sequence;

	public SpeexPacket(ImmutableByteArray2D frames_, String room_, short sequence_) {
		frames = frames_;
		room = room_;
		sequence = sequence_;
	}

	public SpeexPacket(Seal contactKey, ImmutableByteArray2D frames_, String room_, short sequence_) {
		super(contactKey, 0);
		frames = frames_;
		room = room_;
		sequence = sequence_;
	}
}