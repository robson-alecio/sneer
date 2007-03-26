package wheel.io.ui;

import java.awt.SystemTray;
import java.net.URL;

import javax.swing.JOptionPane;

import wheel.io.ui.TrayIcon.SystemTrayNotSupported;

public class SwingUser {

	private final TrayIcon _trayIcon;

	public interface Action {

		String caption();

		void run();

	}


	public SwingUser(URL iconURL) throws SystemTrayNotSupported {
		_trayIcon = new TrayIcon(iconURL);
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
		_trayIcon.seeReminder(reminder);		
	}


	public void addAction(final Action action) {
		_trayIcon.addAction(action);
	}

	

	public void acknowledgeNotification(String notification) {
		choose(notification, "OK");
	}
}
