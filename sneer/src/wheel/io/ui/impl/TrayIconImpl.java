package wheel.io.ui.impl;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.ResourceBundle;

import static sneer.Language.*;

import wheel.io.Log;
import wheel.io.ui.CancelledByUser;
import wheel.io.ui.TrayIcon;
import wheel.lang.exceptions.Catcher;
import wheel.lang.exceptions.PrintStackTracer;

public class TrayIconImpl implements TrayIcon {

	public static class SystemTrayNotSupported extends Exception {

		public SystemTrayNotSupported() {
			super(string("TRAYICON_NOTSUPPORTED"));
		}

		private static final long serialVersionUID = 1L;
	}

	private final java.awt.TrayIcon _trayIcon;

	private final Catcher _catcher;

	public TrayIconImpl(URL icon, Catcher catcherForThrowsDuringActionExecution)
			throws SystemTrayNotSupported {
		if (icon == null)
			throw new InvalidParameterException("Icon cannot be null");

		if (!SystemTray.isSupported())
			throw new SystemTrayNotSupported();

		SystemTray tray = SystemTray.getSystemTray();
		Image image = Toolkit.getDefaultToolkit().getImage(icon);
		java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(image, string("APPLICATION_NAME"),
				new PopupMenu());
		trayIcon.setImageAutoSize(false);
		// trayIcon.addMouseListener(mouseListener);

		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			throw new SystemTrayNotSupported();
		}

		_trayIcon = trayIcon;
		_catcher = catcherForThrowsDuringActionExecution;
	}

	public TrayIconImpl(URL userIcon) throws SystemTrayNotSupported {
		this(userIcon, new PrintStackTracer());
	}

	public void addAction(final Action action) {
		PopupMenu popup = _trayIcon.getPopupMenu();
		if (popup.getItemCount() > 0)
			popup.addSeparator();

		MenuItem menuItem = new MenuItem(action.caption());

		menuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ignored) {
				try {
					action.run();
				} catch (Throwable t) {
					_catcher.catchThis(t);
				}
			}
		});

		popup.add(menuItem);
	}

	public void seeReminder(String reminder) {
		_trayIcon.displayMessage(string("APPLICATION_NAME"), reminder, MessageType.NONE);
	}
	
	public void clearActions(){
		_trayIcon.getPopupMenu().removeAll();
	}
}
