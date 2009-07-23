package spikes.sneer.bricks.skin.audio.loopback.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.ByteArrayOutputStream;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import sneer.bricks.hardware.cpu.lang.contracts.Contract;
import sneer.bricks.hardware.cpu.threads.Steppable;
import sneer.bricks.hardware.cpu.threads.Threads;
import spikes.sneer.bricks.skin.audio.kernel.Audio;

class Player {
	
	static private ByteArrayOutputStream _buffer;
	static private SourceDataLine _sourceDataLine;
	private static Contract _refToAvoidGc;

	static void stop() {
		_refToAvoidGc.dispose();
		if (_sourceDataLine != null)
			_sourceDataLine.close();
	}

	static boolean start(ByteArrayOutputStream buffer) {
		try {
			_sourceDataLine = my(Audio.class).tryToOpenPlaybackLine();
		} catch (LineUnavailableException e) {
			return false;
		}

		_buffer = buffer;
		
		_refToAvoidGc = my(Threads.class).startStepping(new Steppable() { @Override public void step() {
			playBuffer();
		}});
		return true;
	}

	static private void playBuffer() {
		byte[] audioData = readBuffer();
		if (audioData.length == 0) {
			my(Threads.class).sleepWithoutInterruptions(100);
			return;
		}

		_sourceDataLine.write(audioData, 0, audioData.length);
	}


	private static byte[] readBuffer() {
		byte[] result;
		synchronized (_buffer) {
			result = _buffer.toByteArray();
			_buffer.reset();
		}
		return result;
	}

}
