package sneer.bricks.hardware.gui.trayicon.impl;

import java.net.URL;

import sneer.bricks.hardware.gui.trayicon.SystemTrayNotSupported;
import sneer.bricks.hardware.gui.trayicon.TrayIcon;
import sneer.bricks.hardware.gui.trayicon.TrayIcons;

class TrayIconsImpl implements TrayIcons {

	@Override
	public TrayIcon newTrayIcon(URL icon) throws SystemTrayNotSupported {
		return new TrayIconImpl(icon);
	}
}
