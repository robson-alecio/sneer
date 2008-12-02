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
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import sneer.kernel.container.Inject;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.blinkinglights.Light;
import sneer.pulp.blinkinglights.LightType;
import sneer.pulp.threadpool.Stepper;
import sneer.pulp.threadpool.ThreadPool;
import sneer.skin.sound.kernel.Audio;
import sneer.skin.sound.player.SoundPlayer;
import wheel.lang.Threads;
import wheel.lang.exceptions.FriendlyException;
import wheel.lang.exceptions.NotImplementedYet;

class SoundPlayerImpl implements SoundPlayer, Stepper {

	@Inject static private BlinkingLights _lights;
	@Inject static private Audio _audio;
	
	private final Light _light = _lights.prepare(LightType.ERROR);
	
	@Inject
	static private ThreadPool _threads; {
		_threads.registerStepper(this);
	}
	
	final static private Collection<URL> urls = Collections.synchronizedCollection(new ArrayList<URL>());
	
	public void playNext() {
		if (1 == 1) throw new NotImplementedYet();
		URL url = nextUrlToPlay();
		AudioInputStream audioInputStream = tryInitAudioInputStream(url);
		AudioFormat audioFormat = audioInputStream.getFormat();

		int bufferSize = (int) audioFormat.getSampleRate() * audioFormat.getFrameSize();
		byte[] buffer = new byte[bufferSize];

		SourceDataLine dataLine = null;
		try {
			dataLine = _audio.openSourceDataLine(audioFormat);
			int bytesRead = 0;
			while (bytesRead >= 0) {
				bytesRead = audioInputStream.read(buffer, 0, buffer.length);
				if (bytesRead >= 0) {
					dataLine.write(buffer, 0, bytesRead);
				}
			} 
		} catch (IOException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		} catch (LineUnavailableException e) {
			_lights.turnOnIfNecessary(_light, new FriendlyException(e, "Error: audio line is unavailable, can't play a sound!", 
																				  "Get an expert sovereign friend to help you."));
			if (dataLine != null)	dataLine.close();
		} finally {
			if (dataLine == null) return;
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

	@Override
	public boolean step() {
		if(urls.size() > 0) playNext();
		else
			Threads.sleepWithoutInterruptions(50); //Optimize: Use wait/notify.
		return true;
	}

	@Override
	public void play(URL url) {
		urls.add(url);
	}
}