package main;

import sneer.brickness.BrickLoadingException;
import sneer.brickness.Brickness;
import sneer.commons.environments.Environment;
import sneer.commons.environments.EnvironmentUtils;
import sneer.pulp.threads.Threads;

public class SneerSession {

	public static void main(String[] ignored) throws Exception {
		new SneerSession();
	}
	
	public SneerSession() {
		Environment container = Brickness.newBrickContainer(new SneerStoragePath());
		
		loadBricks(container, platformBricks());
		loadBricks(container, snappBricks());
		
		loadBrick(container, Threads.class).waitUntilCrash();
	}

	static public void loadBricks(Environment container, final Class<?>... bricks) throws BrickLoadingException {
		for (Class<?> brick : bricks)
			loadBrick(container, brick);
	}

	private static <T> T loadBrick(Environment container, Class<T> brick) {
		return EnvironmentUtils.retrieveFrom(container, brick);
	}

	public static Class<?>[] platformBricks() {
		return new Class<?>[] {
				sneer.pulp.log.receiver.file.LogToFile.class,
				sneer.pulp.log.receiver.sysout.LogToSysout.class,

				sneer.pulp.clockticker.ClockTicker.class,
		};
	}

	private static Class<?>[] snappBricks() {
		return new Class<?>[] {
				sneer.skin.main.menu.MainMenu.class,
				snapps.welcomewizard.UserInfo.class,
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
				sneer.hardware.log.gui.LogConsole.class,

				snapps.contacts.hardcoded.HardcodedContacts.class,
		};
	}
}