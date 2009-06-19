package sneer.bricks.software.bricks.snappstarter.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.software.bricks.snappstarter.SnappStarter;
import sneer.foundation.brickness.BrickLoadingException;

class SnappStarterImpl implements SnappStarter {

	{
		loadBricks(platformBricks());
		loadBricks(snappBricks());
	}
	
	private void loadBricks(final Class<?>... bricks) throws BrickLoadingException {
		for (Class<?> brick : bricks) my(brick);
	}

	private Class<?>[] platformBricks() {
		return new Class<?>[] {
				sneer.bricks.pulp.log.receiver.file.LogToFile.class,
				sneer.bricks.pulp.log.receiver.sysout.LogToSysout.class,

				sneer.bricks.hardware.clock.ticker.ClockTicker.class,

				sneer.bricks.pulp.connection.SocketOriginator.class,
				sneer.bricks.pulp.connection.SocketReceiver.class,
				sneer.bricks.pulp.probe.ProbeManager.class,
		};
	}

	private Class<?>[] snappBricks() {
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

				sneer.bricks.skin.main.dashboard.Dashboard.class,

				sneer.bricks.hardware.log.gui.LogConsole.class,
				
				sneer.bricks.pulp.log.exceptions.ExceptionLogger.class,
				sneer.bricks.snapps.contacts.hardcoded.HardcodedContacts.class,
				sneer.bricks.snapps.gis.map.gui.MapGui.class,
				
				sneer.bricks.skin.main.menu.exit.ExitMenuItem.class,
		};
	}

}
