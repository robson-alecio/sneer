package snapps.contacts.gui.impl;

import static sneer.commons.environments.Environments.my;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import snapps.contacts.internetaddress.gui.InternetAddressWindow;
import sneer.brickness.PublicKey;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.ContactManager;
import sneer.pulp.keymanager.KeyManager;
import sneer.skin.windowboundssetter.WindowBoundsSetter;

class NewContactWindow extends JFrame{
	
	private final KeyManager _keyManager = my(KeyManager.class);
	private final ContactManager _contactManager = my(ContactManager.class);
	private final JTextField txtNick = new JTextField();
	
	NewContactWindow() {
		super("Contact Nickname:");
		initGui();
	}

	private void initGui() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		final JButton btnOk = new JButton("Ok");
		final JButton btnCancel = new JButton("Cancel");

		getContentPane().setLayout(new GridBagLayout());
		getContentPane().add(txtNick,
				new GridBagConstraints(0, 0, 2, 1, 2.0, 0.0,
						GridBagConstraints.CENTER,	GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),0, 0));

		getContentPane().add(btnOk,
				new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

		getContentPane().add(btnCancel,
				new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

		btnCancel.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) {
			close();
		}});

		btnOk.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) {
			createNewOrEditContact();
		}});

		setSize(300, 150);
		my(WindowBoundsSetter.class).setBestBounds(this);
		setVisible(true);
	}

	private void createNewOrEditContact() {
		setVisible(false);
		String nick = txtNick.getText();
		if (nick == null || nick.trim().length() == 0)
			return;

		Contact contact = _contactManager.contactGiven(nick);
		if (contact == null) {
			contact = _contactManager.addContact(nick);
			_keyManager.addKey(contact, mickeyMouseKey(nick));
		}

		InternetAddressWindow window = my(InternetAddressWindow.class);
		window.setActiveContact(contact);
		window.open();
		
		close();
	}
	
	private void close() {
		setVisible(false);
		dispose();
	}
	
	@SuppressWarnings("deprecation") PublicKey mickeyMouseKey(String nick) {
		return _keyManager.generateMickeyMouseKey(nick);
	}
}