package sneer.skin.sound.player.tests;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.skin.sound.player.SoundPlayer;

public class SoundPlayerDemo {
	
	public static void main(String[] args) {
		Container container = ContainerUtils.getContainer();
		SoundPlayer player = container.produce(SoundPlayer.class);	
		player.play(SoundPlayerDemo.class.getResource("test.wav"));
	}
}