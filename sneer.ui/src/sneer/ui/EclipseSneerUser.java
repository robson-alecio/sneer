package sneer.ui;


import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import sneer.SimpleUser;
import sneer.ui.views.ContactsView;

public class EclipseSneerUser extends SimpleUser {

	public Shell _shell;
	
	private ContactsView _contactsView;

	public EclipseSneerUser(Shell shell) {
		_shell = shell;
	}
	
	public void contactsView(ContactsView view) {
		_contactsView = view;
	}

	protected String answer(String message, String defaultValue) {
		InputDialog dialog = new InputDialog(_shell, "Sneer", message, defaultValue, null);
		dialog.setBlockOnOpen(true);
		if (InputDialog.OK == dialog.open()) {
			return dialog.getValue();
		}
		return defaultValue;
	}

	public void lookAtMe() {
		if (null == _contactsView) return;
		_contactsView.refresh();
	}

	@Override
	protected void lamentError(String message) {
		MessageDialog.openError(_shell, "Sorry", message);
	}

}
