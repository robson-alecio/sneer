package sneer.skin.sound.loopback.impl;

import java.io.ByteArrayOutputStream;

import javax.sound.sampled.AudioFormat;

import sneer.skin.sound.loopback.LoopbackTester;

class LoopbackTesterImpl implements LoopbackTester{

	private final Recorder _recorder;
	private final Player _player;
	private final int DELAY = 3000;

	LoopbackTesterImpl(){
		AudioFormat _audioFormat = new AudioFormat(8000.0F, 16, 1, true, true);
		_recorder = new Recorder(_audioFormat, DELAY);
		_player = new Player(_audioFormat, DELAY);
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