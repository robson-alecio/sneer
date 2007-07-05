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
	
	final static Color selected = new Color(230,240,255);

	@Override
	public Component getListCellRendererComponent(JList list, Object contactObject, int index, boolean isSelected, boolean hasFocus) {

		Contact contact = (Contact) contactObject;

		ImageIcon stateIcon = ERROR_ICON;
		if (contact.state().currentValue().equals(Contact.UNCONFIRMED_STATE))
			stateIcon = UNCONFIRMED_ICON;
		if (contact.state().currentValue().equals(Contact.CONFIRMED_STATE))
			stateIcon = CONFIRMED_ICON;

		System.out.println("Renderer: " + contact.isOnline().currentValue());
		ImageIcon onlineIcon = contact.isOnline().currentValue()
			? ONLINE_ICON
			: OFFLINE_ICON;

		FlowLayout layout = new FlowLayout();
		JPanel panel = new JPanel(layout);
		//if (isSelected) //uncomment this if selection is needed
		//	panel.setBackground(selected);
		//else
			panel.setBackground(Color.white);
		panel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		layout.setAlignment(FlowLayout.LEFT);
		panel.add(new JLabel(onlineIcon));
		panel.add(new JLabel(stateIcon));
		panel.add(new JLabel(contact.nick().currentValue() + " - " + contact.host().currentValue() + ":" + contact.port().currentValue())); //Fix: should use respective printer
		return panel;
	}

	private static final long serialVersionUID = 1L;
}