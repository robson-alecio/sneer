//Copyright (C) 2005 Klaus Wuestefeld and Rodrigo B. de Oliveira
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.

package sneer.ui;


import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import sneer.SimpleUser;
import sneer.ui.views.ContactsView;

public class EclipseSneerUser extends SimpleUser {

	private final Shell _shell;
	
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
		MessageDialog.openError(_shell, "Sneer", message);
	}

	public void acknowledge(String fact) {
		MessageDialog.openInformation(_shell, "Sneer", fact);
	}

	@Override
	protected boolean confirm(String proposition) {
		return MessageDialog.openQuestion(_shell, "Sneer", proposition);
	}

}
