package spikes.sneer.bricks.skin.audio.player.impl;

import static sneer.foundation.environments.Environments.my;

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

import sneer.bricks.hardware.cpu.lang.contracts.Contract;
import sneer.bricks.hardware.cpu.threads.Steppable;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.hardware.io.IO;
import sneer.bricks.hardware.io.log.Logger;
import sneer.foundation.lang.exceptions.NotImplementedYet;
import spikes.sneer.bricks.skin.audio.kernel.Audio;
import spikes.sneer.bricks.skin.audio.player.SoundPlayer;

class SoundPlayerImpl implements SoundPlayer, Steppable {

	private final Audio _audio = my(Audio.class);
	private final List<URL> urls = Collections.synchronizedList(new ArrayList<URL>());
	private Contract _stepperContract;

	@Override
	public void play(URL url) {
		synchronized (urls) {
			urls.add(url);
			if (urls.size() == 1)
				_stepperContract = my(Threads.class).keepStepping(this);
		}
	}

	@Override
	public void step() {
		while (true) {
			doPlay(urls.get(0));
			synchronized (urls) {
				urls.remove(0);
				if (urls.isEmpty()) _stepperContract.dispose();
			}
		}
	}
	
	private void doPlay(URL url) {
		AudioInputStream audioInputStream = null;
		try {
			audioInputStream = tryInitAudioInputStream(url);
		} catch (IOException e) {
			throw new sneer.foundation.lang.exceptions.NotImplementedYet(e); // Fix Use BL
		}
		
		try {
			play(audioInputStream); 
		} finally {
			my(IO.class).crash(audioInputStream);
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

}