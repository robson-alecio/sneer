package sneer.skin.sound.player;

import java.net.URL;

import sneer.brickness.Brick;

@Brick
public interface SoundPlayer {
	
	void play(URL file);
	
}