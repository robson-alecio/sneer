package spikes.sandro.audio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class JavaSoundImplementation implements Sound{

	boolean stopCapture = false;
	ByteArrayOutputStream byteArrayOutputStream;
	AudioFormat audioFormat;
	TargetDataLine targetDataLine;
	AudioInputStream audioInputStream;
	SourceDataLine sourceDataLine;

	public void captureAudio() {
		try {
			audioFormat = getAudioFormat();
			DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
			targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
			targetDataLine.open(audioFormat);
			targetDataLine.start();
			Thread captureThread = new Thread(new CaptureThread());
			captureThread.start();
		} catch (Exception e) {
			System.out.println(e);
			System.exit(0);
		}
	}

	public void playAudio() {
		try {
			byte audioData[] = byteArrayOutputStream.toByteArray();
			InputStream byteArrayInputStream = new ByteArrayInputStream(audioData);
			AudioFormat format = getAudioFormat();
			audioInputStream = new AudioInputStream(byteArrayInputStream,	format, audioData.length / format.getFrameSize());
			DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
			sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
			sourceDataLine.open(format);
			sourceDataLine.start();

			Thread playThread = new Thread(new PlayThread());
			playThread.start();
		} catch (Exception e) {
			System.out.println(e);
			System.exit(0);
		}
	}

	public AudioFormat getAudioFormat() {
		float sampleRate = 8000.0F;	// 8000,11025,16000,22050,44100
		int sampleSizeInBits = 16;	// 8,16
		int channels = 1;	// 1,2
		boolean signed = true;// true,false
		boolean bigEndian = false;// true,false
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}

	class CaptureThread extends Thread {
		byte tempBuffer[] = new byte[10000];

		@Override
		public void run() {
			byteArrayOutputStream = new ByteArrayOutputStream();
			stopCapture = false;
			try {
				while (!stopCapture) {
					int cnt = targetDataLine.read(tempBuffer, 0, tempBuffer.length);
					if (cnt > 0) {
						byteArrayOutputStream.write(tempBuffer, 0, cnt);
					}
				}
				byteArrayOutputStream.close();
			} catch (Exception e) {
				System.out.println(e);
				System.exit(0);
			}
		}
	}

	class PlayThread extends Thread {
		byte tempBuffer[] = new byte[10000];
		
		@Override
		public void run() {
			try {
				int cnt;
				while ((cnt = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
					if (cnt > 0) {
						sourceDataLine.write(tempBuffer, 0, cnt);
					}
				}
				sourceDataLine.drain();
				sourceDataLine.close();
			} catch (Exception e) {
				System.out.println(e);
				System.exit(0);
			}
		}
	}
	
	@Override
	public void stopCapture(boolean stop) {
		stopCapture = stop;
	}
	
	public static void main(String args[]) {
		new AudioCaptureDemoGui(new JavaSoundImplementation()).setVisible(true) ;
	}
}