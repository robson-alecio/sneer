package snapps.contacts.gui.delete.impl;

import static sneer.commons.environments.Environments.my;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import snapps.contacts.actions.ContactAction;
import snapps.contacts.actions.ContactActionManager;
import snapps.contacts.gui.ContactsGui;
import snapps.contacts.gui.delete.DeleteContactWindow;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.ContactManager;
import sneer.skin.windowboundssetter.WindowBoundsSetter;

class DeleteContactWindowImpl extends JFrame implements DeleteContactWindow {
	
	private final JButton _yes = new JButton("Yes");

	DeleteContactWindowImpl(){
		initGui();
		addContactEditAction();
	}
	
	private void addContactEditAction() {
		my(ContactActionManager.class).addContactAction(new ContactAction(){
			@Override public boolean isEnabled() { return true; }
			@Override public boolean isVisible() { return true; }
			@Override public String caption() { return "Delete Contact...";}
			@Override public void run() {
				checkAndDelete();
			}});
	}

	private void initGui() {
		setLayout(new GridBagLayout());
		JButton no = new JButton("No");
		no.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent e) {
			setVisible(false);
		}});
		
		add(0, _yes);
		add(1, no);
	}

	private void add(int x, JButton btn) {
		getContentPane().add(btn,  new GridBagConstraints(x, 0, 1, 1, 0.0, 0.0, 
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5), 0, 0) );
	}
	
	@Override
	public void checkAndDelete() {
		final Contact contact = my(ContactsGui.class).selectedContact().currentValue();
		if(contact==null) return;
		
		setSize(300, 100);
		my(WindowBoundsSetter.class).setBestBounds(this);
		setVisible(true);
		setTitle("Delete '" + contact + "'?");
		_yes.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent e) {
			deleteContact(contact);
			setVisible(false);
		}});
	}
	
	private void deleteContact(Contact contact) {
		String nick = contact.nickname().currentValue();
		System.out.println(nick);
		//Fix: NPE
		setVisible(false);
		my(ContactManager.class).removeContact(nick);
	}
}
