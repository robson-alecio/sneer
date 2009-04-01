package sneer.skin.sound.player;

import java.net.URL;

import sneer.brickness.OldBrick;

public interface SoundPlayer extends OldBrick{
	
	void play(URL file);
	
}