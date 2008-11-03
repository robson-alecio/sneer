package snapps.listentome.speex;

import sneer.pulp.tuples.Tuple;

public class SpeexPacket extends Tuple {
	
	public final byte[][] _frames;

	public SpeexPacket(byte[][] frames) {
		_frames = frames;
	}

}
