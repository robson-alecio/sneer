package sneer.skin.sound.player;

import java.net.URL;

import sneer.brickness.Brick;

public interface SoundPlayer extends Brick{
	
	void play(URL file);
	
}