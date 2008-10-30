package wheel.io.ui.impl;

import static wheel.i18n.Language.translate;

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

import wheel.io.ui.TrayIcon;
import wheel.io.ui.action.Action;
import wheel.io.ui.action.ActionUtility;
import wheel.io.ui.action.ReactiveAction;
import wheel.io.ui.graphics.Images;
import wheel.lang.exceptions.Catcher;
import wheel.lang.exceptions.PrintStackTracer;

public class TrayIconImpl implements TrayIcon {

	public static class SystemTrayNotSupported extends Exception {

		public SystemTrayNotSupported() {
			super(translate("System Tray Icon not supported. Might be running under a fancy Linux window manager."));
		}

		private static final long serialVersionUID = 1L;
	}

	private final java.awt.TrayIcon _trayIcon;

	private final Catcher _catcher;

	private ReactiveAction _defaultAction;

	public TrayIconImpl(URL icon, Catcher catcherForThrowsDuringActionExecution)
			throws SystemTrayNotSupported {
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
		_catcher = catcherForThrowsDuringActionExecution;
	}

	private java.awt.TrayIcon createTrayIcon(Image image) {
		java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(image, translate("Sneer"),
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

	public TrayIconImpl(URL userIcon) throws SystemTrayNotSupported {
		this(userIcon, new PrintStackTracer());
	}

	public void addAction(Action action) {
		addAction(ActionUtility.getReativeActionWrapper(action));
	}
	
	public void addAction(final ReactiveAction action) {
		PopupMenu popup = _trayIcon.getPopupMenu();
		if (popup.getItemCount() > 0)
			popup.addSeparator();

		final MenuItem menuItem = new MenuItem(action.caption());
		
		menuItem.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent ignored) {
			try {
				action.run();
			} catch (Throwable t) {
				_catcher.catchThis(t);
			}
		}});
		popup.add(menuItem);
	}

	public void messageBalloon(String title, String message) {
		_trayIcon.displayMessage(title, message, MessageType.NONE);
	}
	
	public void clearActions(){
		_trayIcon.getPopupMenu().removeAll();
	}

	public void setDefaultAction(ReactiveAction defaultAction) {
		_defaultAction = defaultAction;
	}

	public void setDefaultAction(final Action defaultAction) {
		_defaultAction = ActionUtility.getReativeActionWrapper(defaultAction);
	}

	public void dispose() {
		SystemTray.getSystemTray().remove(_trayIcon);		
	}
}
