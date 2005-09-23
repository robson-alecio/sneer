//Copyright (C) 2005 Klaus Wuestefeld and Rodrigo B. de Oliveira
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.

package sneer.ui;


import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import sneer.SimpleUser;
import sneer.ui.views.SneerView;

public class EclipseSneerUser extends SimpleUser {

	private final Shell _shell;
	
	private SneerView _contactsView;

	private Map<String, Chat> chats = new HashMap<String,Chat>();

	public EclipseSneerUser(Shell shell) {
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
			Chat chat = new Chat(chatShell, SWT.NONE) {

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
	
	public static void main(String[] args) {
		Display d = Display.getDefault();
		
		Shell chatShell = new Shell(d);
		chatShell.setSize(400, 500);
		Chat chat = new Chat(chatShell, SWT.NONE) {

			@Override
			protected void sendMessage(String text) {
			}
			
		};
		
		
		chatShell.open();
		while(!chatShell.isDisposed() && !d.isDisposed()) {
			if (!d.readAndDispatch()) {
				d.sleep();
			}
		}
		d.dispose();
	}

}
