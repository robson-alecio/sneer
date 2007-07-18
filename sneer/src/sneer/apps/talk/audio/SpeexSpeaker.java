package sneer.apps.talk.audio;

import java.io.StreamCorruptedException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import org.xiph.speex.SpeexDecoder;

import wheel.io.Log;

public class SpeexSpeaker {
	
	private SourceDataLine _line;
	private AudioFormat _format;

	private SpeexDecoder _decoder = new SpeexDecoder();
	byte[] _pcmBuffer = new byte[2048]; //Big enough :P

	
	public SpeexSpeaker() throws LineUnavailableException {
		_line = AudioUtil.getSourceDataLine();
		_line.open();
		_line.start();
		
		_format = _line.getFormat();
		_decoder.init(AudioUtil.NARROWBAND_ENCODING, (int) _format.getFrameRate(), _format.getChannels(), false);
	}


	public synchronized void sendAudio(byte[][] frames, int lagDecay) {
		lagDecay = lagDecay * AudioUtil.SAMPLE_SIZE_IN_BITS / 8 * AudioUtil.CHANNELS;
		
		for (int t = 0; t < frames.length; t++) {
			byte[] frame = frames[t];
			
			try {
				_decoder.processData(frame, 0, frame.length);
			} catch (StreamCorruptedException e) {
				throw new IllegalArgumentException(e);
			}
			int processed = _decoder.getProcessedData(_pcmBuffer, 0);
			_line.write(_pcmBuffer, 0, processed - lagDecay);
		}
	}

	public void close() {
		_line.close();
	}

}
