package spikes.demos;

import static sneer.foundation.environments.Environments.my;
import sneer.foundation.brickness.Brickness;
import sneer.foundation.environments.Environments;
import spikes.sneer.bricks.skin.audio.player.SoundPlayer;

public class SoundPlayerDemo {
	
	private final SoundPlayer _player = my(SoundPlayer.class);
	
	SoundPlayerDemo() {
		_player.play(SoundPlayerDemo.class.getResource("alert1.wav"));
		_player.play(SoundPlayerDemo.class.getResource("alert2.wav"));
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			throw new sneer.foundation.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	}
	
	public static void main(String[] args) {
		Environments.runWith(Brickness.newBrickContainer(), new Runnable(){ @Override public void run() {
			try {
				new SoundPlayerDemo();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}});
	}
}
