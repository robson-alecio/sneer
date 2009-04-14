package sneer.hardware.gui.trayicon.impl;

import static sneer.commons.environments.Environments.my;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.security.InvalidParameterException;

import sneer.hardware.gui.trayicon.SystemTrayNotSupported;
import sneer.hardware.gui.trayicon.TrayIcon;
import sneer.pulp.exceptionhandling.ExceptionHandler;
import wheel.io.ui.action.Action;
import wheel.io.ui.graphics.Images;

class TrayIconImpl implements TrayIcon {

	private final java.awt.TrayIcon _trayIcon;

	private Action _defaultAction;

	TrayIconImpl(URL icon) throws SystemTrayNotSupported {
		if (icon == null)
			throw new InvalidParameterException("Icon cannot be null");

		if (!SystemTray.isSupported())
			throw new SystemTrayNotSupported();

		SystemTray tray = SystemTray.getSystemTray();
		Image image = Images.getImage(icon);
		java.awt.TrayIcon trayIcon = createTrayIcon(image);
		// trayIcon.addMouseListener(mouseListener);

		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			throw new SystemTrayNotSupported();
		}

		_trayIcon = trayIcon;
	}

	private java.awt.TrayIcon createTrayIcon(Image image) {
		java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(image, "Sneer",
				new PopupMenu());
		trayIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 1){
					if (_defaultAction != null)
						_defaultAction.run();
				}
			}
		});
		trayIcon.setImageAutoSize(false);
		return trayIcon;
	}

	
	public void addAction(final Action action) {
		PopupMenu popup = _trayIcon.getPopupMenu();
		if (popup.getItemCount() > 0)
			popup.addSeparator();

		final MenuItem menuItem = new MenuItem(action.caption());
		
		menuItem.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent ignored) {
			my(ExceptionHandler.class).shield(new Runnable() { @Override public void run() {
				action.run();
			}});
		}});
		popup.add(menuItem);
	}

	public void messageBalloon(String title, String message) {
		_trayIcon.displayMessage(title, message, MessageType.NONE);
	}
	
	public void clearActions(){
		_trayIcon.getPopupMenu().removeAll();
	}

	public void setDefaultAction(Action defaultAction) {
		_defaultAction = defaultAction;
	}

	public void dispose() {
		SystemTray.getSystemTray().remove(_trayIcon);		
	}

}
