package snapps.listentome.speex.impl;

import org.xiph.speex.SpeexEncoder;

import snapps.listentome.speex.SpeexCodec;
import snapps.listentome.speex.SpeexPacket;
import sneer.kernel.container.Inject;
import sneer.pulp.tuples.TupleSpace;
import sneer.skin.sound.PcmSoundPacket;
import sneer.skin.sound.kernel.impl.AudioUtil;
import wheel.lang.Omnivore;

class SpeexCodecImpl implements SpeexCodec {

	private byte[][] _frames = newFramesArray();
	private final SpeexEncoder _encoder = new SpeexEncoder();
	private int _frameIndex;

	@Inject
	static private TupleSpace _tupleSpace; 
	
	public SpeexCodecImpl() {
		_encoder.init(AudioUtil.NARROWBAND_ENCODING, AudioUtil.SOUND_QUALITY, 8000, 1);
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
		if (!_encoder.processData(pcmBuffer, 0, pcmBuffer.length)) return false;
		
		byte[] speexBuffer = new byte[_encoder.getProcessedDataByteSize()]; //Speex will always fit in the pcm space because it is compressed.
		_encoder.getProcessedData(speexBuffer, 0);
		_frames[_frameIndex++] = speexBuffer;
		return _frameIndex == AudioUtil.FRAMES_PER_AUDIO_PACKET;
	}

}
