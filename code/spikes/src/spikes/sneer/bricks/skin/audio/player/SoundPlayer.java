package spikes.sneer.bricks.skin.audio.player;

import java.net.URL;

import sneer.foundation.brickness.Brick;

@Brick
public interface SoundPlayer {
	
	void play(URL file);
	
}