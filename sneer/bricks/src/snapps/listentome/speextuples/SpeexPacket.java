package snapps.listentome.speextuples;

import sneer.pulp.keymanager.PublicKey;
import sneer.pulp.tuples.Tuple;

public class SpeexPacket extends Tuple {
	
	public final byte[][] _frames;

	public SpeexPacket(byte[][] frames) {
		_frames = frames;
	}

	public SpeexPacket(PublicKey contactKey, byte[][] frames) {
		super(contactKey, 0);
		_frames = frames;
	}

}
