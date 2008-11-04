package snapps.listentome.speex.impl;

import org.xiph.speex.SpeexEncoder;

import snapps.listentome.speex.Encoder;
import sneer.skin.sound.kernel.impl.AudioUtil;

public class EncoderImpl implements Encoder {

	private final SpeexEncoder _encoder = new SpeexEncoder();
	
	EncoderImpl() {
		_encoder.init(AudioUtil.NARROWBAND_ENCODING, AudioUtil.SOUND_QUALITY, 8000, 1);
	}

	@Override
	public byte[] getProcessedData() {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public boolean processData(byte[] pcmBuffer) {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}
}
