package sneer.pulp.microphone.impl;

//import java.util.Arrays;
//
//import javax.sound.sampled.LineUnavailableException;
//import javax.sound.sampled.TargetDataLine;
//
//import org.xiph.speex.SpeexEncoder;
//
//import sneer.apps.talk.audio.AudioCommon;
//import sneer.apps.talk.audio.AudioUtil;
//import sneer.pulp.audio.PcmSoundPacket;
import sneer.pulp.microphone.Microphone;
//import wheel.io.Logger;
//import wheel.reactive.EventSource;

public class MicrophoneImpl extends Thread implements Microphone {

//	private static final int SAMPLE_RATE_IN_HZ = 8000;
//	private static final int SAMPLE_SIZE_IN_BITS = 16;
//	private static final int NUMBER_OF_CHANNELS = 2;
//	private static final int ONE_HUNDRETH_OF_A_SECOND = SAMPLE_RATE_IN_HZ / 100;
//	
//	private TargetDataLine _line;
//
//	private volatile boolean _running = true;
//
//
//	@Override
//	public void run() {
//		try {
//			tryToRun();
//		} catch (RuntimeException e) {
//			if (!_running) return;
//			Logger.log(e);
//		}
//	}
//
//	private void tryToRun() {
//		byte[] pcmBuffer = new byte[
//			SAMPLE_SIZE_IN_BITS / 8 *
//			NUMBER_OF_CHANNELS *
//			ONE_HUNDRETH_OF_A_SECOND
//		];
//
//		while (_running) {
//			int read = _line.read(pcmBuffer, 0, pcmBuffer.length);
//
//			int processed = _encoder.getProcessedData(speexBuffer, 0);
//			frames[frameIndex++] = Arrays.copyOf(speexBuffer, processed);
//			
//			_consumer.audio(frames);
//			frameIndex = 0;
//		}
//	}
//
//
//	public interface AudioConsumer {
//		public void audio(byte[][] buffer);
//	}
//
//
//	@Override
//	public EventSource<byte[]> capturedSound() {
//		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
//	}
//
	@Override
	synchronized public void close() {
//		_running = false;
//		_line.close();
	}
//
	@Override
	synchronized public void open() {
//		_line = AudioCommon.bestAvailableTargetDataLine();
//		_line.open();
//		_line.start();
//		
//		setDaemon(true);
//		start();
	}

}
