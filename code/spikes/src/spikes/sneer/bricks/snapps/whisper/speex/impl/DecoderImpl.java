package spikes.sneer.bricks.snapps.whisper.speex.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.StreamCorruptedException;

import org.xiph.speex.SpeexDecoder;

import spikes.sneer.bricks.skin.audio.kernel.Audio;
import spikes.sneer.bricks.snapps.whisper.speex.Decoder;

class DecoderImpl implements Decoder {
	
	private final SpeexDecoder _decoder = new SpeexDecoder();
	
	private final Audio _audio = my(Audio.class);
	
	DecoderImpl() {
		_decoder.init(SpeexConstants.NARROWBAND_ENCODING, (int)_audio.defaultAudioFormat().getFrameRate(), _audio.defaultAudioFormat().getChannels(), _audio.defaultAudioFormat().isBigEndian());
	}

	public byte[] getProcessedData() {
		final byte[] buffer = new byte[_decoder.getProcessedDataByteSize()];
		_decoder.getProcessedData(buffer, 0);
		return buffer;
	}

	@Override
	public byte[][] decode(byte[][] frames) {
		final byte[][] result = new byte[frames.length][];
		for (int i=0; i<frames.length; ++i)
			result[i] = decode(frames[i]);
		return result;
	}

	private byte[] decode(final byte[] frame) {
		processData(frame);
		return getProcessedData();
	}

	private void processData(final byte[] frame) {
		try {
			_decoder.processData(frame, 0, frame.length);
		} catch (StreamCorruptedException e) {
			throw new sneer.foundation.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	}
}
