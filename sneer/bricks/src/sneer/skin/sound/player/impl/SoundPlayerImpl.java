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
	
	final static private Collection<URL> urls = Collections.synchronizedCollection(new ArrayList<URL>());
	
	public void playNext() {
		URL url = nextUrlToPlay();
		AudioInputStream audioInputStream = tryInitAudioInputStream(url);
		AudioFormat audioFormat = audioInputStream.getFormat();
		SourceDataLine dataLine = tryInitSourceDataLine(audioFormat);

		int bufferSize = (int) audioFormat.getSampleRate() * audioFormat.getFrameSize();
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
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		} finally {
			dataLine.drain();
			dataLine.stop();
			dataLine.close();
		}
	}

	private URL nextUrlToPlay() {
		Iterator<URL> iterator = urls.iterator();
		URL url = iterator.next();
		iterator.remove();
		return url;
	}

	private AudioInputStream tryInitAudioInputStream(URL url) {
		try {
			return AudioSystem.getAudioInputStream(url);
		} catch (UnsupportedAudioFileException e) {
			throw new IllegalArgumentException("Unsupported Audio: " + url.toString()); //Implement
		} catch (IOException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	} 

	private SourceDataLine tryInitSourceDataLine(	AudioFormat audioFormat) {
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
		SourceDataLine dataLine = null;
		try{
			dataLine = (SourceDataLine) AudioSystem	.getLine(info);
			dataLine.open(audioFormat);
			dataLine.start();
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