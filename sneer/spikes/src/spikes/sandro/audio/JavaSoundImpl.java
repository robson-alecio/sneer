package spikes.sandro.audio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class JavaSoundImpl implements Sound{

	private AudioFormat _audioFormat = new AudioFormat(8000.0F, 16, 1, true, false);
	private ByteArrayOutputStream _buffer;
	private boolean _stopCapture = false;
	private boolean _stopPlay = false;

	@Override
	public void startRecord() {
		new CaptureThread().start();
	}
	
	@Override
	public void stopRecord() {
		_stopCapture = true;
	}

	@Override
	public void stopPlay() {
		_stopPlay = true;
	}	
	
	@Override
	public void startPlay() {
		new PlayThread().start();
	}
	
	private void record() throws LineUnavailableException {
		byte tempBuffer[] = new byte[10000];
		DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, _audioFormat);
		TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
		targetDataLine.open(_audioFormat);
		targetDataLine.start();
		
		_buffer = new ByteArrayOutputStream();
		_stopCapture = false;
		while (!_stopCapture) {
			int cnt = targetDataLine.read(tempBuffer, 0, tempBuffer.length);
			if (cnt > 0) _buffer.write(tempBuffer, 0, cnt);
		}
		System.out.println("Stop Record!");
	}	
	
	private SourceDataLine play() throws LineUnavailableException, IOException {
		int cnt;
		byte tempBuffer[] = new byte[10000];
		DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class,	_audioFormat);
		SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
		sourceDataLine.open(_audioFormat);
		sourceDataLine.start();

		_stopPlay = false;
		while (!_stopPlay) {
			byte[] audioData = _buffer.toByteArray();
			_buffer = new ByteArrayOutputStream();
			AudioInputStream _toPlayInputStream = new AudioInputStream(new ByteArrayInputStream(audioData), 
														  _audioFormat, audioData.length / _audioFormat.getFrameSize());
			while ((cnt = _toPlayInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
				if (cnt > 0) sourceDataLine.write(tempBuffer, 0, cnt);
			}
		}
		System.out.println("Stop Play!");
		sourceDataLine.drain();
		return sourceDataLine;
	}
	
	private class CaptureThread extends Thread {
		@Override
		public void run() {
			try {
				record();
			} catch (Exception e) {
				System.out.println(e);
				System.exit(0);
			} finally{
				try { _buffer.close(); } catch (IOException e) {/* ignore */}
			}
		}
	}

	private class PlayThread extends Thread {
		@Override
		public void run() {
			SourceDataLine sourceDataLine = null;
			try {
				sourceDataLine = play();
			} catch (Exception e) {
				System.out.println(e);
				System.exit(0);
			} finally{
				try { sourceDataLine.close(); } catch (Exception e) {/* ignore */}
			}
		}
	}
}