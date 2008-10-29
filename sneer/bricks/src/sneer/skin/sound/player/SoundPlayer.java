package sneer.skin.sound.player;

import java.net.URL;

import sneer.kernel.container.Brick;

public interface SoundPlayer extends Brick{
	
	void play(URL file);
	
}