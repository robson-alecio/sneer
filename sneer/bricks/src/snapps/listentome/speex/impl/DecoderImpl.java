package snapps.listentome.speex.impl;

import java.io.StreamCorruptedException;

import javax.sound.sampled.AudioFormat;

import org.xiph.speex.SpeexDecoder;

import snapps.listentome.speex.Decoder;
import sneer.skin.sound.kernel.impl.AudioUtil;

public class DecoderImpl implements Decoder {
	
	private final SpeexDecoder _decoder = new SpeexDecoder();
	
	DecoderImpl(AudioFormat format) {
		_decoder.init(AudioUtil.NARROWBAND_ENCODING, (int) format.getFrameRate(), format.getChannels(), false);
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
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	}
}
