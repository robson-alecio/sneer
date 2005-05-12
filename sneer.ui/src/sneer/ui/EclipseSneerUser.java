package sneer.ui;

import java.io.IOException;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import sneer.Sneer.User;

public class EclipseSneerUser implements User {

	private Shell _shell;

	public EclipseSneerUser(Shell shell) {
		_shell = shell;
	}

	public String name() {
		return input("Sneer User", "Your name");
	}

	private String input(String defaultValue, String message) {
		InputDialog dialog = new InputDialog(_shell, "Sneer", message, null, null);
		dialog.setBlockOnOpen(true);
		if (InputDialog.OK == dialog.open()) {
			return dialog.getValue();
		}
		return defaultValue;
	}

	public String giveNickname() {
		return input("friend", "Give your new contact a nickname");
	}

	public String informTcpAddress() {
		return input("localhost", "What is your contact's address? host:port");
	}

	public void lamentException(IOException e) {
		e.printStackTrace();
		MessageDialog.openError(_shell, "Sorry", e.toString());
	}

	public void checkOutNewContacts() {
		// TODO Auto-generated method stub
		
	}

}
