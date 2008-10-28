package sneer.skin.sound.impl;

import java.io.ByteArrayOutputStream;

import javax.sound.sampled.AudioFormat;

import sneer.skin.sound.Loopback;

class LoopbackImpl implements Loopback{

	private final Recorder _recorder;
	private final Player _player;
	private final int DELAY = 3000;

	LoopbackImpl(){
		AudioFormat _audioFormat = new AudioFormat(8000.0F, 16, 1, true, true);
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		_recorder = new Recorder(_audioFormat, buffer, DELAY);
		_player = new Player(_audioFormat, buffer, DELAY);
	}
	
	@Override
	public void close() {
		_recorder.stopRecorder();
		_player.stopPlayer();
	}

	@Override
	public void open() {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		_recorder.startRecorder(buffer);
		_player.startPlayer(buffer);
	}	
}