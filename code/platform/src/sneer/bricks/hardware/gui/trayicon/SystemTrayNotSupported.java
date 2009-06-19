package sneer.bricks.hardware.gui.trayicon;

public class SystemTrayNotSupported extends Exception {

	public SystemTrayNotSupported() {
		super("System Tray Icon not supported. Might be running under a fancy Linux window manager.");
	}

}
