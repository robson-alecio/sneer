package sneer.bricks.snapps.contacts.gui.delete.impl;

import static sneer.foundation.environments.Environments.my;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import sneer.bricks.network.social.Contact;
import sneer.bricks.network.social.ContactManager;
import sneer.bricks.skin.windowboundssetter.WindowBoundsSetter;
import sneer.bricks.snapps.contacts.actions.ContactAction;
import sneer.bricks.snapps.contacts.actions.ContactActionManager;
import sneer.bricks.snapps.contacts.gui.ContactsGui;
import sneer.bricks.snapps.contacts.gui.delete.DeleteContactWindow;
import sneer.foundation.lang.ByRef;

class DeleteContactWindowImpl extends JFrame implements DeleteContactWindow {
	
	private final JButton _yes = new JButton("Yes");
	
	DeleteContactWindowImpl(){
		initGui();
		addContactEditAction();
	}
	
	private void addContactEditAction() {

		final ByRef<Contact> contactRef = ByRef.newInstance();
		_yes.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent e) {
			my(ContactManager.class).removeContact(contactRef.value);
			setVisible(false);
		}});
		
		my(ContactActionManager.class).addContactAction(new ContactAction(){
			@Override public boolean isEnabled() { return true; }
			@Override public boolean isVisible() { return true; }
			@Override public String caption() { return "Delete Contact...";}
			@Override public void run() {
				Contact contact = my(ContactsGui.class).selectedContact().currentValue();
				contactRef.value = contact;
				
				if(contact==null) return;
				open(contact);
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
	
	private void open(final Contact contact) {
		setSize(300, 100);
		my(WindowBoundsSetter.class).setBestBounds(this, my(ContactActionManager.class).baseComponent());
		setVisible(true);
		setTitle("Delete '" + contact + "'?");
	}
}