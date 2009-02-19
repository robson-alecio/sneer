package snapps.whisper.speextuples;

import sneer.kernel.container.PublicKey;
import sneer.kernel.container.Tuple;

public class SpeexPacket extends Tuple {
	
	public final byte[][] frames;
	public final String room;
	public final short sequence;

	public SpeexPacket(byte[][] frames_, String room_, short sequence_) {
		frames = frames_;
		room = room_;
		sequence = sequence_;
	}

	public SpeexPacket(PublicKey contactKey, byte[][] frames_, String room_, short sequence_) {
		super(contactKey, 0);
		frames = frames_;
		room = room_;
		sequence = sequence_;
	}
}