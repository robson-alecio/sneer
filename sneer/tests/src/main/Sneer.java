package main;

import static sneer.commons.environments.Environments.my;
import sneer.brickness.BrickLoadingException;
import sneer.brickness.Brickness;
import sneer.commons.environments.Environment;
import sneer.commons.environments.Environments;


public class Sneer {

	public static void main(String[] ignored) throws Exception {
		new Sneer();
	}
	
	public Sneer() {
		Environment container = Brickness.newBrickContainer(new SneerStoragePath());
		
		loadBricks(container, businessBricks());
		loadBricks(container, communicationBricks());
	}

	static public void loadBricks(Environment container, final Class<?>... bricks) throws BrickLoadingException {
		Environments.runWith(container, new Runnable() { @Override public void run() {
			for (Class<?> brick : bricks) my(brick);
		}});
	}

	static public Class<?>[] businessBricks() {
		return new Class<?>[] {
				sneer.pulp.log.workers.notifier.LogNotifier.class,
				sneer.pulp.log.receiver.file.LogToFile.class,
				sneer.pulp.log.receiver.sysout.LogToSysout.class,
				sneer.pulp.threads.Threads.class,
		};
	}

	public static Class<?>[] communicationBricks() {
		return new Class<?>[] {
				sneer.skin.main.menu.MainMenu.class,
				snapps.welcomewizard.WelcomeWizard.class,
				snapps.contacts.gui.delete.DeleteContactWindow.class,
				snapps.contacts.gui.ContactsGui.class,
				snapps.contacts.gui.info.ContactInfoWindow.class,
				snapps.wind.gui.WindGui.class,
				snapps.whisper.gui.WhisperGui.class,
				snapps.meter.memory.gui.MemoryMeterGui.class,
				snapps.meter.bandwidth.gui.BandwidthMeterGui.class,
				snapps.blinkinglights.gui.BlinkingLightsGui.class,
				sneer.hardware.gui.trayicon.TrayIcons.class,
				sneer.skin.main.dashboard.Dashboard.class,
				snapps.contacts.hardcoded.HardcodedContacts.class,
				sneer.hardware.log.gui.LogConsole.class,
		};
	}
}