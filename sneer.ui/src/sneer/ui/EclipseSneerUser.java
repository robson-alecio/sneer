package sneer.ui;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.widgets.Shell;

import sneer.Sneer.User;

public class EclipseSneerUser implements User {

	private Shell _shell;

	public EclipseSneerUser(Shell shell) {
		_shell = shell;
	}

	public String promptName() {
		InputDialog dialog = new InputDialog(_shell, "Sneer", "Your name", null, null);
		dialog.setBlockOnOpen(true);
		if (InputDialog.OK == dialog.open()) {
			return dialog.getValue();
		}
		return "Sneer User";
	}

}
