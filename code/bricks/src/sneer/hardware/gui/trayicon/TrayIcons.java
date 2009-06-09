package sneer.hardware.gui.trayicon;

import java.net.URL;

import sneer.brickness.Brick;

@Brick
public interface TrayIcons {

	TrayIcon newTrayIcon(URL userIcon) throws SystemTrayNotSupported;

}