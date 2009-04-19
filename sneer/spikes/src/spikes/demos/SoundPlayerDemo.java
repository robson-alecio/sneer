package spikes.demos;

import static sneer.commons.environments.Environments.my;
import sneer.brickness.testsupport.SystemBrickEnvironment;
import sneer.commons.environments.Environments;
import sneer.pulp.logging.out.LogToSystemOut;
import sneer.skin.sound.player.SoundPlayer;

public class SoundPlayerDemo {
	
	private final SoundPlayer _player = my(SoundPlayer.class);
	
	SoundPlayerDemo() {
		my(LogToSystemOut.class);

		_player.play(SoundPlayerDemo.class.getResource("alert1.wav"));
		_player.play(SoundPlayerDemo.class.getResource("alert2.wav"));
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			throw new sneer.commons.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	}
	
	public static void main(String[] args) {
		Environments.runWith(new SystemBrickEnvironment(), new Runnable(){ @Override public void run() {
			try {
				new SoundPlayerDemo();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}});
	}
}
