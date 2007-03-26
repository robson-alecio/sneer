package wheel.io.ui;

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

import wheel.io.ui.SwingUser.Action;

public class TrayIcon {

	public static class SystemTrayNotSupported extends Exception { private static final long serialVersionUID = 1L; }

	
	private final java.awt.TrayIcon _trayIcon;
	
	public TrayIcon(URL icon) throws SystemTrayNotSupported {
		
		if (icon == null) throw new InvalidParameterException("Icon must not be null.");
		
		if (!SystemTray.isSupported()) throw new SystemTrayNotSupported();
		
		SystemTray tray = SystemTray.getSystemTray();
	    Image image = Toolkit.getDefaultToolkit().getImage(icon);
	    java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(image, "Sneer", new PopupMenu());	            
	    trayIcon.setImageAutoSize(false);
//	    trayIcon.addMouseListener(mouseListener);
	    
		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			throw new SystemTrayNotSupported();
		}
		
		_trayIcon = trayIcon;
	}
	
	public void addAction(final Action action) {
		PopupMenu popup = _trayIcon.getPopupMenu();
		if (popup.getItemCount() > 0) popup.addSeparator();
		
		MenuItem menuItem = new MenuItem(action.caption());

		menuItem.addActionListener(
		    new ActionListener() {
		    	public void actionPerformed(ActionEvent ignored) {
		    		action.run();
		    	}
		    }
		);

		popup.add(menuItem);
	}
	
	public void seeReminder(String reminder) {
		_trayIcon.displayMessage("Sneer", reminder, MessageType.NONE);		
	}
}
