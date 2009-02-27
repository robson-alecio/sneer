package snapps.whisper.speex.impl;

import org.xiph.speex.SpeexEncoder;

import snapps.whisper.speex.Encoder;
import sneer.skin.sound.kernel.Audio;
import static sneer.brickness.Environments.my;

class EncoderImpl implements Encoder {

	private final SpeexEncoder _encoder = new SpeexEncoder();
	
	private final Audio _audio = my(Audio.class);
	
	EncoderImpl() {
		_encoder.init(SpeexConstants.NARROWBAND_ENCODING, SpeexConstants.SOUND_QUALITY, (int) _audio.defaultAudioFormat().getFrameRate(), _audio.defaultAudioFormat().getChannels());
	}

	@Override
	public byte[] getProcessedData() {
		byte[] speexBuffer = new byte[_encoder.getProcessedDataByteSize()];
		_encoder.getProcessedData(speexBuffer, 0);
		return speexBuffer;
	}

	@Override
	public boolean processData(byte[] pcmBuffer) {
		return _encoder.processData(pcmBuffer, 0, pcmBuffer.length);
	}

}
