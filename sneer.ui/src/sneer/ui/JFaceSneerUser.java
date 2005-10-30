//Copyright (C) 2005 Klaus Wuestefeld and Rodrigo B. de Oliveira
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.

package sneer.ui;


import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.progress.UIJob;

import sneer.SimpleUser;
import sneer.ui.views.SneerView;

public class JFaceSneerUser extends SimpleUser {

	private final Shell _shell;
	
	private SneerView _contactsView;

	private Map<String, Chat> chats = new HashMap<String,Chat>();

	public JFaceSneerUser(Shell shell) {
		_shell = shell;
	}
	
	public void contactsView(SneerView view) {
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
	
	@Override
	protected String browseForFile(String message) {
		FileDialog fileDialog = new FileDialog(_shell, SWT.OPEN);
		fileDialog.setText(message);
		fileDialog.setFilterExtensions(new String[] { "*.jpg", "*.*" });
		fileDialog.setFilterNames(new String[] { "JPEG Image (*.jpg)", "All Files (*.*)" });
        return fileDialog.open();
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

	public boolean confirm(final String proposition) {
		final boolean[] resultHolder = new boolean[1];
		UIJob job = new UIJob("Contact refresh") {
			public IStatus runInUIThread(IProgressMonitor monitor) {
				resultHolder[0] = MessageDialog.openQuestion(_shell, "Sneer", proposition);
				return Status.OK_STATUS;
			}						
		};
		job.setSystem(true);
		job.schedule();
		
		try {
			job.join();
		} catch (InterruptedException e) {
			throw new RuntimeException(e); //TODO Change to wheel's SpanishInquisition.
		}
		return resultHolder[0];
	}

	public void goodbye() {
		if (null == _contactsView) return;
		_contactsView.stop();		
	}
	
	public Chat getChatForContact(final String contact) {
		if (chats.get(contact) == null) {
			Shell chatShell = new Shell(_shell.getDisplay());
			chatShell.setLayout(new FillLayout());
			chatShell.setText("Chat with "+ contact);
			chatShell.setSize(400, 500);
			Chat chat = new Chat(chatShell, SWT.NONE, contact) {

				@Override
				protected void sendMessage(String text) {
					SneerUIPlugin.sneer().sendMessage(contact, text);
					
				}
				
			};
			chatShell.addDisposeListener(new DisposeListener() {

				public void widgetDisposed(DisposeEvent e) {
					chats.remove(contact);
				}
				
			});
			chatShell.open();
			chats.put(contact, chat);
			return getChatForContact(contact);
		}
		return chats.get(contact);
		
	}

	public void receiveMessage(String message, String sender) {
		getChatForContact(sender).addCorrespondence(message);
	}

}
