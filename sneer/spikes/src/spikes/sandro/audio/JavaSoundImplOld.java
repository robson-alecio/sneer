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

public class JavaSoundImplOld implements Sound{

	private AudioFormat _audioFormat = new AudioFormat(8000.0F, 16, 1, true, true);
	private ByteArrayOutputStream _buffer = new ByteArrayOutputStream();
	private boolean _stopCapture = false;
	private boolean _stopPlay = false;

	@Override
	public void startRecord() {
		new Recorder().start();
	}
	
	@Override
	public void startPlay() {
		new Player().start();
	}
	
	@Override
	public void stopRecord() {
		_stopCapture = true;
	}

	@Override
	public void stopPlay() {
		_stopPlay = true;
	}	
	
	
	private class Recorder extends Thread {
		@Override 
		public void run() {
			System.out.println("Start Record!");
			TargetDataLine targetDataLine = initDataLine();
			
			_stopCapture = false;
			while (!_stopCapture) {
				appendBytesInBuffer(targetDataLine);
			}
			
			System.out.println("Stop Record!");
			finalizeDataLine(targetDataLine);
		}
		
		private TargetDataLine initDataLine(){
			DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, _audioFormat);
			TargetDataLine targetDataLine = null;
			try {
				targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
				targetDataLine.open(_audioFormat);
			}  catch (LineUnavailableException e) {
				if(targetDataLine !=null)  
					targetDataLine.close();
				throw new IllegalStateException("Can't Play!", e);
			} 
			targetDataLine.start();
			return targetDataLine;
		}

		private void appendBytesInBuffer(TargetDataLine targetDataLine) {
			byte tmpArray[] = new byte[10000];
			int cnt = targetDataLine.read(tmpArray, 0, tmpArray.length);
			if (cnt > 0){
				_buffer.write(tmpArray, 0, cnt);
				System.out.println(_buffer.size() + " bytes recorded...");				
			}
		}
		
		private void finalizeDataLine(TargetDataLine dataLine) {
			try { 
				dataLine.stop();
				dataLine.drain();
				dataLine.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private class Player extends Thread {
		@Override 
		public void run() {
			System.out.println("Start Play!");
			SourceDataLine dataLine = initDataLine();

			_stopPlay = false;
			while (!_stopPlay) {
				byte[] audioData = readBytesAndCreateNewBuffer();
				if(audioData.length == 0){
					nothigToPlayTrySleep();
					continue;
				}
				
				System.out.println(audioData.length + " bytes to play");		
				AudioInputStream source = new AudioInputStream(new ByteArrayInputStream(audioData), _audioFormat,
																				 audioData.length / _audioFormat.getFrameSize());
				playBytesFromBuffer(dataLine, source);
			}
			
			System.out.println("Stop Play!");
			finalizeDataLine(dataLine);
		}

		private SourceDataLine initDataLine() {
			SourceDataLine sourceDataLine = null;
			DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class,	_audioFormat);
			try {
				sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
				sourceDataLine.open(_audioFormat);
			} catch (LineUnavailableException e) {
				if(sourceDataLine !=null)  
					sourceDataLine.close();
				throw new IllegalStateException("Can't Play!", e);
			} 
			sourceDataLine.start();
			return sourceDataLine;
		}
		
		private byte[] readBytesAndCreateNewBuffer() {
			byte[] audioData = _buffer.toByteArray();
			_buffer = new ByteArrayOutputStream();
			return audioData;
		}
		
		private void nothigToPlayTrySleep() {
			System.out.println("nothing to play...");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				throw new sneer.commons.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
			}
		}
		
		private void playBytesFromBuffer(SourceDataLine dataLine, 	AudioInputStream _sourceStream) {
			try {
				int cnt;
				byte tmpBytes[] = new byte[10000];
				while ((cnt = _sourceStream.read(tmpBytes, 0, tmpBytes.length)) != -1) {
					if (cnt > 0) dataLine.write(tmpBytes, 0, cnt);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
		
		private void finalizeDataLine(SourceDataLine dataLine) {
			try { 
				dataLine.stop();
				dataLine.drain();
				dataLine.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}