package sneer.hardware.gui.trayicon.impl;

import java.net.URL;

import sneer.hardware.gui.trayicon.SystemTrayNotSupported;
import sneer.hardware.gui.trayicon.TrayIcon;
import sneer.hardware.gui.trayicon.TrayIcons;

class TrayIconsImpl implements TrayIcons {

	@Override
	public TrayIcon newTrayIcon(URL icon) throws SystemTrayNotSupported {
		return new TrayIconImpl(icon);
	}
}
