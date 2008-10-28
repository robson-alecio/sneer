package sneer.skin.sound.impl;

import java.io.ByteArrayOutputStream;

import javax.sound.sampled.AudioFormat;

import sneer.skin.sound.Loopback;

class LoopbackImpl implements Loopback{

	private final Recorder _recorder;
	private final Player _player;

	LoopbackImpl(){
		AudioFormat _audioFormat = new AudioFormat(8000.0F, 16, 1, true, true);
		ByteArrayOutputStream _buffer = new ByteArrayOutputStream();
		
		_recorder = new Recorder(_audioFormat, _buffer);
		_player = new Player(_audioFormat, _buffer);
	}
	
	@Override
	public void startRecord() {
		_recorder.startRecorder();
	}
	
	@Override
	public void stopRecord() {
		_recorder.stopRecorder();
	}
	
	@Override
	public void startPlayer() {
		_player.startPlayer();
	}

	@Override
	public void stopPlayer() {
		_player.stopPlayer();
	}	
}