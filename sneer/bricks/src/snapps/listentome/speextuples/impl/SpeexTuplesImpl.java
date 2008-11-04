package snapps.listentome.speextuples.impl;

import snapps.listentome.speex.Encoder;
import snapps.listentome.speex.Speex;
import snapps.listentome.speextuples.SpeexPacket;
import snapps.listentome.speextuples.SpeexTuples;
import sneer.kernel.container.Inject;
import sneer.pulp.tuples.TupleSpace;
import sneer.skin.sound.PcmSoundPacket;
import sneer.skin.sound.kernel.impl.AudioUtil;
import wheel.lang.Omnivore;

class SpeexTuplesImpl implements SpeexTuples {

	@Inject
	private static Speex _speex;
	private byte[][] _frames = newFramesArray();
	private final Encoder _encoder = _speex.newEncoder();
	private int _frameIndex;

	@Inject
	static private TupleSpace _tupleSpace; 
	
	public SpeexTuplesImpl() {
		_tupleSpace.addSubscription(PcmSoundPacket.class, new Omnivore<PcmSoundPacket>() { @Override public void consume(PcmSoundPacket packet) {
			if (encode(packet.payload.copy()))
				flush();
		}});
	}

	private static byte[][] newFramesArray() {
		return new byte[AudioUtil.FRAMES_PER_AUDIO_PACKET][];
	}

	private void flush() {
		_tupleSpace.publish(new SpeexPacket(_frames));
		_frames = newFramesArray();
		_frameIndex = 0;
	}

	private boolean encode(final byte[] pcmBuffer) {
		if (!_encoder.processData(pcmBuffer)) return false;
		
		_frames[_frameIndex++] = _encoder.getProcessedData();
		return _frameIndex == AudioUtil.FRAMES_PER_AUDIO_PACKET;
	}

}
