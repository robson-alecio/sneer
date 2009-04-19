package sneer.skin.sound.player.impl;

import static sneer.commons.environments.Environments.my;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import sneer.commons.io.Streams;
import sneer.commons.lang.exceptions.NotImplementedYet;
import sneer.pulp.threadpool.Stepper;
import sneer.pulp.threadpool.ThreadPool;
import sneer.skin.sound.kernel.Audio;
import sneer.skin.sound.player.SoundPlayer;
import sneer.pulp.logging.Logger;
import wheel.lang.Threads;

class SoundPlayerImpl implements SoundPlayer {

	private final Audio _audio = my(Audio.class);
	
	final static private List<URL> urls = Collections.synchronizedList(new ArrayList<URL>());

	public SoundPlayerImpl() {
		my(ThreadPool.class).registerStepper(new Stepper(){ @Override public boolean step() {
			playNextIfAvailable();
			return true;
		}});
	}
	
	
	private void playNextIfAvailable() {
		if (urls.isEmpty()) {
			Threads.sleepWithoutInterruptions(50); //Optimize: Use wait/notify.
			return;
		}

		URL url = urls.remove(0);
		
		AudioInputStream audioInputStream = null;
		try {
			audioInputStream = tryInitAudioInputStream(url);
		} catch (IOException e) {
			throw new sneer.commons.lang.exceptions.NotImplementedYet(e); // Fix Use BL
		}
		
		try {
			play(audioInputStream); 
		} finally {
			Streams.crash(audioInputStream);
		} 
	}

	private void play(AudioInputStream audioInputStream) {
		AudioFormat audioFormat = audioInputStream.getFormat();

		SourceDataLine dataLine;
		try {
			dataLine = _audio.tryToOpenPlaybackLine(audioFormat);
		} catch (LineUnavailableException e) {
			return;
		}
		dataLine.start();

		play(audioInputStream, audioFormat, dataLine);
	}

	private void play(AudioInputStream audioInputStream, AudioFormat audioFormat, SourceDataLine dataLine) {
		int bufferSize = (int) audioFormat.getSampleRate() * audioFormat.getFrameSize();
		byte[] buffer = new byte[bufferSize];

		int bytesRead = 0;
		while (true) {
			bytesRead = read(audioInputStream, buffer);
			if (bytesRead == -1) return;
			
			dataLine.write(buffer, 0, bytesRead);
		}
	}

	private int read(AudioInputStream audioInputStream, byte[] buffer) {
		try {
			return audioInputStream.read(buffer, 0, buffer.length);
		} catch (IOException e) {
			my(Logger.class).log(e); //Implement Make this a blinking light.
			return -1;
		}
	}

	private AudioInputStream tryInitAudioInputStream(URL url) throws IOException {
		try {
			return AudioSystem.getAudioInputStream(url);
		} catch (UnsupportedAudioFileException e) {
			throw new NotImplementedYet(); //Implement BL
		}
	} 

	@Override
	public void play(URL url) {
		urls.add(url);
	}
}