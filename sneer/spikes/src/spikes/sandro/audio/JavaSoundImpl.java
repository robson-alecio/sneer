package spikes.sandro.audio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class JavaSoundImpl implements Sound{

	private AudioFormat _audioFormat = new AudioFormat(8000.0F, 16, 1, true, false);
	private ByteArrayOutputStream _buffer = new ByteArrayOutputStream();
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
	
	private TargetDataLine record() throws Exception {
		System.out.println("Start Record!");
		byte tempBuffer[] = new byte[10000];
		DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, _audioFormat);
		TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
		targetDataLine.open(_audioFormat);
		targetDataLine.start();
		
		_stopCapture = false;
		while (!_stopCapture) {
			int cnt = targetDataLine.read(tempBuffer, 0, tempBuffer.length);
			if (cnt > 0){
				_buffer.write(tempBuffer, 0, cnt);
				System.out.println(_buffer.size() + " bytes recorded...");				
			}
			
		}
		System.out.println("Stop Record!");
		return targetDataLine;
	}	
	
	private SourceDataLine play() throws Exception {
		System.out.println("Start Play!");
		int cnt;
		byte tempBuffer[] = new byte[10000];
		DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class,	_audioFormat);
		SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
		sourceDataLine.open(_audioFormat);
		sourceDataLine.start();

		_stopPlay = false;
		while (!_stopPlay) {
			byte[] audioData = _buffer.toByteArray();
			if(audioData.length == 0){
				System.out.println("nothing to play...");
				Thread.sleep(1000);
				continue;
			}
			System.out.println(audioData.length + " bytes to play");				
			_buffer = new ByteArrayOutputStream();

			AudioInputStream _toPlayInputStream = new AudioInputStream(new ByteArrayInputStream(audioData), 
														  _audioFormat, audioData.length / _audioFormat.getFrameSize());
			while ((cnt = _toPlayInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
				if (cnt > 0) sourceDataLine.write(tempBuffer, 0, cnt);
			}
		}
		System.out.println("Stop Play!");
		return sourceDataLine;
	}
	
	private class CaptureThread extends Thread {
		@Override
		public void run() {
			TargetDataLine targetDataLine = null;
			try {
				targetDataLine = record();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			} finally{
				try { 
					targetDataLine.stop();
					targetDataLine.drain();
					targetDataLine.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
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
				e.printStackTrace();
				System.exit(0);
			} finally{
				try { 
					sourceDataLine.stop();
					sourceDataLine.drain();
					sourceDataLine.close(); 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}