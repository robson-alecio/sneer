package sneer.bricks.hardware.gui.trayicon;

import java.net.URL;

import sneer.foundation.brickness.Brick;

@Brick
public interface TrayIcons {

	TrayIcon newTrayIcon(URL userIcon) throws SystemTrayNotSupported;

}