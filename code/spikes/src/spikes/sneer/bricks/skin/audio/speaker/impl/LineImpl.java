package spikes.sneer.bricks.skin.audio.speaker.impl;

import static sneer.foundation.environments.Environments.my;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import spikes.sneer.bricks.skin.audio.kernel.Audio;
import spikes.sneer.bricks.skin.audio.speaker.Speaker.Line;

class LineImpl implements Line {

	LineImpl() throws LineUnavailableException {
		_delegate = my(Audio.class).tryToOpenPlaybackLine();
	}

	
	private final SourceDataLine _delegate;

	
	@Override
	public void consume(byte[] packet) {
		_delegate.write(packet, 0, packet.length);
	}
	
	@Override
	public void close() {
		_delegate.close();
	}

}