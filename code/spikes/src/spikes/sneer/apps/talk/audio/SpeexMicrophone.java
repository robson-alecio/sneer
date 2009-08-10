package spikes.sneer.apps.talk.audio;

import static sneer.foundation.environments.Environments.my;

import java.util.Arrays;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import org.xiph.speex.SpeexEncoder;

import sneer.bricks.hardware.io.log.exceptions.ExceptionLogger;


public class SpeexMicrophone extends Thread {

	private final AudioConsumer _consumer;

	private final TargetDataLine _line;
	private final AudioFormat _format;
	private final SpeexEncoder _encoder = new SpeexEncoder();

	private volatile boolean _running = true;


	public SpeexMicrophone(AudioConsumer consumer) throws LineUnavailableException {
		_consumer = consumer;
		
		_line = AudioCommon.bestAvailableTargetDataLine();
		_line.open();
		_line.start();
		
		_format = _line.getFormat();
		_encoder.init(AudioUtil.NARROWBAND_ENCODING, AudioUtil.SOUND_QUALITY, (int) _format.getFrameRate(), _format.getChannels());

		System.out.println("Frame size: " + _encoder.getFrameSize());

		
		setDaemon(true);
		start();
	}

	@Override
	public void run() {
		try {
			tryToRun();
		} catch (RuntimeException e) {
			if (!_running) return;
			my(ExceptionLogger.class).log(e);
		}
	}

	private void tryToRun() {
		byte[][] frames = new byte[AudioUtil.FRAMES_PER_AUDIO_PACKET][];
		byte[] pcmBuffer = new byte[AudioUtil.SAMPLE_SIZE_IN_BITS / 8 * _encoder.getFrameSize() * _encoder.getChannels()];
		byte[] speexBuffer = new byte[pcmBuffer.length]; //Speex will always fit in the pcm space because it is compressed.

		int frameIndex = 0;
		while (_running) {
			int read = _line.read(pcmBuffer, 0, pcmBuffer.length);

			if (!_encoder.processData(pcmBuffer, 0, read)) continue;
			
			int processed = _encoder.getProcessedData(speexBuffer, 0);
			frames[frameIndex++] = Arrays.copyOf(speexBuffer, processed);
			
			if (frameIndex < AudioUtil.FRAMES_PER_AUDIO_PACKET) continue;
			
			_consumer.audio(frames);
			frameIndex = 0;
		}
	}

	public interface AudioConsumer {
		public void audio(byte[][] buffer);
	}

	public void close() {
		_running = false;
		_line.close();
	}

}
