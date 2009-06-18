/**
 * 
 */
package sneer.bricks.skin.main.dashboard.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.gui.Action;
import sneer.bricks.hardware.gui.trayicon.SystemTrayNotSupported;
import sneer.bricks.hardware.gui.trayicon.TrayIcon;
import sneer.bricks.hardware.gui.trayicon.TrayIcons;
import sneer.bricks.pulp.blinkinglights.BlinkingLights;
import sneer.bricks.pulp.blinkinglights.LightType;
import sneer.bricks.skin.main.dashboard.impl.DashboardImpl.WindowSupport;

class TrayIconSupport {
	private final WindowSupport _windowSupport;

	TrayIconSupport(WindowSupport windowSupport){
		_windowSupport = windowSupport;
		TrayIcon trayIcon = null;
		try {
			trayIcon = my(TrayIcons.class).newTrayIcon(IconUtil.getLogoURL());
			addOpenWindowAction(trayIcon);
			addExitAction(trayIcon);
		} catch (SystemTrayNotSupported e1) {
			my(BlinkingLights.class).turnOn(LightType.INFO, "Minimizing Sneer Window", 
														  e1.getMessage() + " When closing the Sneer window, it will be minimized instead of closed.");
			_windowSupport.changeWindowCloseEventToMinimizeEvent();
		}
	}
	
	private void addOpenWindowAction(TrayIcon tray) {
		Action cmd = new Action(){
			@Override public String caption() { return "Open"; }
			@Override public void run() {	_windowSupport.open();						
		}};
		tray.setDefaultAction(cmd);
		tray.addAction(cmd);
	}
	
	private void addExitAction(TrayIcon trayIcon) {
		Action cmd = new Action(){
			@Override public String caption() { return "Exit"; }
			@Override public void run() {	System.exit(0);
		}};
		trayIcon.addAction(cmd);
	}
}