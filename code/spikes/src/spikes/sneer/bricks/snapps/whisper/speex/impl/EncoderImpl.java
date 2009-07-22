package spikes.sneer.bricks.snapps.whisper.speex.impl;

import static sneer.foundation.environments.Environments.my;

import javax.sound.sampled.AudioFormat;

import org.xiph.speex.SpeexEncoder;

import spikes.sneer.bricks.skin.audio.kernel.Audio;
import spikes.sneer.bricks.snapps.whisper.speex.Encoder;

class EncoderImpl implements Encoder {

	private final SpeexEncoder _encoder = new SpeexEncoder();
	private AudioFormat _defaultAudioFormat = my(Audio.class).defaultAudioFormat();

	
	EncoderImpl() {
		_encoder.init(SpeexConstants.NARROWBAND_ENCODING, SpeexConstants.SOUND_QUALITY, (int) _defaultAudioFormat.getFrameRate(), _defaultAudioFormat.getChannels());
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
