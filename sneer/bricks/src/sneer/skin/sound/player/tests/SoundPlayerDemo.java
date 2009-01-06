package sneer.skin.sound.player.tests;

import static wheel.lang.Environments.my;
import sneer.kernel.container.Containers;
import sneer.skin.sound.player.SoundPlayer;
import wheel.io.Logger;
import wheel.lang.Environments;

public class SoundPlayerDemo {
	
	private final SoundPlayer _player = my(SoundPlayer.class);
	
	SoundPlayerDemo(){
		_player.play(SoundPlayerDemo.class.getResource("alert1.wav"));
		_player.play(SoundPlayerDemo.class.getResource("alert2.wav"));
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	}
	
	public static void main(String[] args) {
		Logger.redirectTo(System.out);
		Environments.runWith(Containers.newContainer(), new Runnable(){ @Override public void run() {
			try {
				new SoundPlayerDemo();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}});
	}
}
