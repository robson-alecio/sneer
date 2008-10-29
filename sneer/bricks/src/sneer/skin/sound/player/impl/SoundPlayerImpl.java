package sneer.skin.sound.player.impl;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import sneer.kernel.container.Inject;
import sneer.pulp.threadpool.ThreadPool;
import sneer.skin.sound.player.SoundPlayer;

class SoundPlayerImpl implements SoundPlayer, Runnable {

	@Inject
	static private ThreadPool _threads;
	{
		_threads.registerActor(this);
	}
	
	Collection<URL> urls = Collections.synchronizedCollection(new ArrayList<URL>());
	
	public void playNext() {
		Iterator<URL> iterator = urls.iterator();
		URL url = iterator.next();
		iterator.remove();
		AudioInputStream audioInputStream = tryInitAudioInputStream(url);
		AudioFormat audioFormat = audioInputStream.getFormat();
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
		
		checkFileFormat(url, info);
		SourceDataLine dataLine = tryInitSourceDataLine(audioFormat, info);
		dataLine.start();

		int bufferSize = (int) audioFormat.getSampleRate() 	* audioFormat.getFrameSize();
		byte[] buffer = new byte[bufferSize];

		try {
			int bytesRead = 0;
			while (bytesRead >= 0) {
				bytesRead = audioInputStream.read(buffer, 0, buffer.length);
				if (bytesRead >= 0) {
					dataLine.write(buffer, 0, bytesRead);
				}
			} 
		} catch (IOException e) {
			throw new IllegalStateException("IOException: " + url, e);
		} finally {
			dataLine.drain();
			dataLine.stop();
			dataLine.close();
		}
	}

	private void checkFileFormat(URL url, DataLine.Info info) {
		if (!AudioSystem.isLineSupported(info)) {
			throw new IllegalArgumentException("Unsupported Audio: " + url.toString()); //Implement
		}
	}
	
	private AudioInputStream tryInitAudioInputStream(URL url) {
		try {
			return AudioSystem.getAudioInputStream(url);
		} catch (UnsupportedAudioFileException e) {
			throw new IllegalStateException("Can't Play this Audio Format! " + url, e);
		} catch (IOException e) {
			throw new IllegalStateException("IOException: " + url, e);
		}
	} 

	private SourceDataLine tryInitSourceDataLine(	AudioFormat audioFormat, DataLine.Info info) {
		SourceDataLine dataLine = null;
		try{
			dataLine = (SourceDataLine) AudioSystem	.getLine(info);
			dataLine.open(audioFormat);
		} catch (LineUnavailableException e) {
				if (dataLine != null)	dataLine.close();
				throw new IllegalStateException("Can't Play!", e);
		}
		return dataLine;
	}

	@Override
	public void run() {
		while(true){
			if(urls.size()>0) playNext();
			else
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
				}
		}
	}

	@Override
	public void play(URL url) {
		urls.add(url);
	}
}