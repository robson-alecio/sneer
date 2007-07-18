package sneer.apps.talk.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import org.xiph.speex.SpeexDecoder;

import wheel.io.Log;

public class SpeexSpeaker extends Thread {
	private AudioFormat _format;

	private SourceDataLine _line;

	private boolean _running = true;

	private SpeexDecoder _decoder = new SpeexDecoder();

	byte[] _decodeBuffer = new byte[2048];

	public SpeexSpeaker() {
		setDaemon(true);
	}

	public void init() throws LineUnavailableException {
		_format = AudioUtil.getFormat();
		_line = AudioUtil.getSourceDataLine();
		_line.open(_format);
		_line.start();
		_decoder.init(0, (int) _format.getFrameRate(), _format.getChannels(), false);
		start();
		while (!isAlive()) {
			try {
				sleep(200);
			} catch (InterruptedException ie) {
			}
		}
		;
	}

	@Override
	public void run() {
		while (_running) {
			try {
				Thread.sleep(500);
			} catch (Exception e) {
			}
		}
		_line.close();
	}

	public synchronized void sendAudio(byte[][] frames) { //Fix: this should not block. implement producer/consumer buffer

		for (int t = 0; t < AudioUtil.FRAMES; t++) {
			byte[] frame = frames[t];
			
			try {
				_decoder.processData(frame, 0, frame.length);
				int processed = _decoder.getProcessedData(_decodeBuffer, 0);
				_line.write(_decodeBuffer, 0, processed);
			} catch (Exception e) {
				Log.log(e);
				e.printStackTrace();
			}
		}
	}

	public void close() {
		_running = false;
	}

}
