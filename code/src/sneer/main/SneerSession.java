package sneer.main;

import sneer.bricks.pulp.threads.Threads;
import sneer.foundation.brickness.BrickLoadingException;
import sneer.foundation.brickness.Brickness;
import sneer.foundation.commons.environments.Environment;
import sneer.foundation.commons.environments.EnvironmentUtils;

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
				sneer.bricks.pulp.log.receiver.file.LogToFile.class,
				sneer.bricks.pulp.log.receiver.sysout.LogToSysout.class,

				sneer.bricks.pulp.clockticker.ClockTicker.class,
		};
	}

	private static Class<?>[] snappBricks() {
		return new Class<?>[] {
				sneer.bricks.skin.main.menu.MainMenu.class,
				sneer.bricks.snapps.welcomewizard.UserInfo.class,
				sneer.bricks.snapps.contacts.gui.delete.DeleteContactWindow.class,
				sneer.bricks.snapps.contacts.gui.ContactsGui.class,
				sneer.bricks.snapps.contacts.gui.info.ContactInfoWindow.class,
				sneer.bricks.snapps.wind.gui.WindGui.class,
				sneer.bricks.snapps.whisper.gui.WhisperGui.class,
				sneer.bricks.snapps.meter.memory.gui.MemoryMeterGui.class,
				sneer.bricks.snapps.meter.bandwidth.gui.BandwidthMeterGui.class,
				sneer.bricks.snapps.blinkinglights.gui.BlinkingLightsGui.class,
				sneer.bricks.hardware.gui.trayicon.TrayIcons.class,
				sneer.bricks.skin.main.dashboard.Dashboard.class,
				sneer.bricks.hardware.log.gui.LogConsole.class,
				sneer.bricks.software.code.compilers.gui.RebuildMenu.class,
				sneer.bricks.snapps.contacts.hardcoded.HardcodedContacts.class,
				sneer.bricks.snapps.gis.map.gui.MapGui.class,
		};
	}
}