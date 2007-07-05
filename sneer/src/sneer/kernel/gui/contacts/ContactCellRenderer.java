package sneer.kernel.gui.contacts;

import javax.swing.*;
import javax.swing.border.*;

import sneer.kernel.business.contacts.Contact;

import java.awt.*;
import java.util.*;

public class ContactCellRenderer extends DefaultListCellRenderer {

	final static ImageIcon ONLINE_ICON = new ImageIcon(ContactCellRenderer.class.getResource("/images/operator.gif"));

	final static ImageIcon OFFLINE_ICON = new ImageIcon(ContactCellRenderer.class.getResource("/images/operator_disabled.gif"));

	final static ImageIcon ERROR_ICON = new ImageIcon(ContactCellRenderer.class.getResource("/images/redled.gif"));

	final static ImageIcon UNCONFIRMED_ICON = new ImageIcon(ContactCellRenderer.class.getResource("/images/yellowled.gif"));

	final static ImageIcon CONFIRMED_ICON = new ImageIcon(ContactCellRenderer.class.getResource("/images/greenled.gif"));

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {

		if (value instanceof Contact) {
			Contact contact = (Contact) value;

			ImageIcon stateIcon = ERROR_ICON;
			if (contact.state().currentValue().equals(Contact.UNCONFIRMED_STATE))
				stateIcon = UNCONFIRMED_ICON;
			if (contact.state().currentValue().equals(Contact.CONFIRMED_STATE))
				stateIcon = CONFIRMED_ICON;

			ImageIcon onlineIcon = OFFLINE_ICON;
			if (contact.isOnline().currentValue())
				onlineIcon = ONLINE_ICON;

			FlowLayout layout = new FlowLayout();
			JPanel panel = new JPanel(layout);
			panel.setBackground(Color.white);
			panel.setBorder(new LineBorder(Color.LIGHT_GRAY,1));
			layout.setAlignment(FlowLayout.LEFT);
			panel.add(new JLabel(onlineIcon));
			panel.add(new JLabel(stateIcon));
			panel.add(new JLabel(contact.nick().currentValue() + " - " + contact.host().currentValue() + ":" + contact.port().currentValue())); //Fix: should use respective printer
			return panel;
		}
		return new JLabel("what?");
	}

	private static final long serialVersionUID = 1L;
}