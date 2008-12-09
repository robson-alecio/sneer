package snapps.listentome.speextuples;

import sneer.pulp.keymanager.PublicKey;
import sneer.pulp.tuples.Tuple;

public class SpeexPacket extends Tuple {
	
	public final byte[][] frames;
	public final String room;

	public SpeexPacket(byte[][] frames_, String room_) {
		frames = frames_;
		room = room_;
	}

	public SpeexPacket(PublicKey contactKey, byte[][] frames_, String room_) {
		super(contactKey, 0);
		frames = frames_;
		room = room_;
	}
}