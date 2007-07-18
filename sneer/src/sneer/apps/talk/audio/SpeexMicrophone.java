package sneer.apps.talk.audio;

import java.util.Arrays;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import org.xiph.speex.SpeexEncoder;

import wheel.lang.Threads;

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
		byte[][] frames = new byte[AudioUtil.FRAMES][];

		int frameIndex = 0;

		while (true) {
			
			int read = _line.read(buffer, 0, buffer.length); //pcm data / 16 bits

			if (_encoder.processData(buffer, 0, read)) {
				
				int processed = _encoder.getProcessedData(buffer, 0);
				frames[frameIndex++] = Arrays.copyOf(buffer, processed);

			}

			if (frameIndex == AudioUtil.FRAMES) {

				if (!_running) break;
				_callback.audio(frames);

				frameIndex = 0;
			}

			if (_counter++ % 10 == 0)
				Threads.sleepWithoutInterruptions(10); //Implement: send a timestamped packet and get it back  to measure lag. Add more/longer delays between mic samples the lag increases.

		}
		_line.close();
	}

	public interface AudioCallback {
		public void audio(byte[][] buffer);
	}

	public void close() {
		_running = false;
	}

}
