package sneer.apps.talk.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import org.xiph.speex.SpeexEncoder;

import wheel.lang.Threads;

// AUDIO FORMAT:
// Sequence of a fixed number (AudioUtil.FRAMES) speex decoded frames,
// each frame with the structure:
// - Header - short, 2 bytes
// - Content - decoded frame

public class SpeexMicrophone extends Thread {

	private AudioFormat _format;

	private TargetDataLine _line;

	private volatile boolean _running = true;

	private AudioCallback _callback;

	private SpeexEncoder _encoder = new SpeexEncoder();

	private int _counter = 0;

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
		while (!isAlive()) { //Refactor: consider if this wait is necessary.
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
		int average = 0;

		while (true) {
			
			int read = _line.read(buffer, 0, buffer.length); //pcm data / 16 bits

			average = average + calculateAverage16BitsPcm(buffer, read);

			if (_encoder.processData(buffer, 0, read)) {
				
				int processed = _encoder.getProcessedData(frameBuffer, frameBufferIndex + 2);
				AudioUtil.shortToByte(frameBuffer, frameBufferIndex, processed);
				frameBufferIndex = frameBufferIndex + processed + 2;
				frameIndex++;
				//System.out.println("encoding "+frameIndex+" - "+processed);
			}

			if (frameIndex == AudioUtil.FRAMES) {
				byte[] contents = new byte[frameBufferIndex];
				System.arraycopy(frameBuffer, 0, contents, 0, frameBufferIndex);
				
				if (!_running) break;
				_callback.audio(contents);

				frameIndex = 0;
				average = 0;
				frameBufferIndex = 0;
			}

			if (_counter++ % 10 == 0)
				Threads.sleepWithoutInterruptions(10); //Implement: send a timestamped packet and get it back  to measure lag. Add more/longer delays between mic samples the lag increases.

		}
		_line.close();
	}

	public int calculateAverage16BitsPcm(byte[] buffer, int length) {
		int total = 0;
		for (int t = 0; t < (length / 2); t++) {
			total += AudioUtil.byteToShort(buffer, t * 2);
		}
		return total / (length / 2);
	}

	public interface AudioCallback {
		public void audio(byte[] buffer);
	}

	public void close() {
		_running = false;
	}

}
