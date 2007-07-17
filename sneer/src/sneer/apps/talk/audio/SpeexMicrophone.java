package sneer.apps.talk.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import org.xiph.speex.SpeexEncoder;

// AUDIO FORMAT:
// Sequence of a fixed number (AudioUtil.FRAMES) speex decoded frames,
// each frame with the structure:
// - Header - short, 2 bytes
// - Content - decoded frame

public class SpeexMicrophone extends Thread {

	private AudioFormat _format;

	private TargetDataLine _line;

	private boolean _running = true;

	private AudioCallback _callback;

	private SpeexEncoder _encoder = new SpeexEncoder();

	public SpeexMicrophone(AudioCallback callback) {
		setDaemon(true);
		_callback = callback;
	}

	public void init() throws LineUnavailableException {
		_format = AudioUtil.getFormat();
		_line = AudioUtil.getTargetDataLine();
		_line.open(_format);
		_line.start();
		_encoder.init(0, 8, (int) _format.getFrameRate(), _format.getChannels());
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
		byte[] buffer = new byte[2 * _encoder.getFrameSize() * _encoder.getChannels()];
		byte[] frameBuffer = new byte[buffer.length * AudioUtil.FRAMES];
		int frameIndex = 0;
		int frameBufferIndex = 0;
		while (_running) {
			int read = _line.read(buffer, 0, buffer.length); //pega audio pcm puro, 16 bits 2 bytes=onda
			
			System.out.println(AudioUtil.byteToShort(buffer, 20));
					
			if (_encoder.processData(buffer, 0, read)) {
				int processed = _encoder.getProcessedData(frameBuffer, frameBufferIndex + 2);
				AudioUtil.shortToByte(frameBuffer, frameBufferIndex, processed);
				frameBufferIndex += processed + 2;
				frameIndex++;
				//System.out.println("encoding "+frameIndex+" - "+processed);
			}
			if (frameIndex == AudioUtil.FRAMES) {
				_callback.audio(frameBuffer, 0, frameBufferIndex);
				frameBufferIndex = 0;
				frameIndex = 0;
			}

		}
		_line.close();
	}

	public interface AudioCallback {
		public void audio(byte[] buffer, int offset, int length);
	}

	public void close() {
		_running = false;
	}

}
