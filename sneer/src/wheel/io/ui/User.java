package wheel.io.ui;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.JOptionPane;

public class User {

	public interface Action {

		String caption();

		void run();

	}


	private TrayIcon _trayIcon;

	public User() {
	}

	public User(URL iconURL) {
		sysTray(iconURL);
	}

	public boolean choose(String proposition, Object... options) {
		int chosen = JOptionPane.showOptionDialog(null, proposition + "\n\n", "Sneer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		return chosen == 0;
	}


	public void acknowledgeUnexpectedProblem(String description) {
		JOptionPane.showMessageDialog(null, description, "Sneer - Unexpected Problem", JOptionPane.ERROR_MESSAGE);
	}

	
	public String answer(String prompt) {
		return answer(prompt, "");
	}

	
	public String answer(String prompt, String defaultAnswer) {
		return JOptionPane.showInputDialog(prompt, defaultAnswer);
	}

	
	public void seeReminder(String reminder) {
		trayIcon().displayMessage("Sneer", reminder, MessageType.NONE);		
	}


	private void sysTray(URL iconURL) {
		if (!SystemTray.isSupported()) return;
		
	    SystemTray tray = SystemTray.getSystemTray();
	    Image image = Toolkit.getDefaultToolkit().getImage(iconURL);
	    TrayIcon trayIcon = new TrayIcon(image, "Sneer", new PopupMenu());	            
	    trayIcon.setImageAutoSize(false);
//	    trayIcon.addMouseListener(mouseListener);
	    try {
			tray.add(trayIcon);
			_trayIcon = trayIcon;
		} catch (AWTException e1) {
			//_trayIcon will be null;
		}		
	}

	
	public void addAction(final Action action) {
		PopupMenu popup = trayIcon().getPopupMenu();
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

	private TrayIcon trayIcon() {
		if (_trayIcon == null) throw new UnsupportedOperationException("SystemTray support required.");
		return _trayIcon;
	}

	public void acknowledgeNotification(String notification) {
		choose(notification, "OK");
	}
}
