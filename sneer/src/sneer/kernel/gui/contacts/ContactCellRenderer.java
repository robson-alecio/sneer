package sneer.kernel.gui.contacts;

import javax.swing.*;
import javax.swing.border.*;

import sneer.kernel.business.contacts.ContactAttributes;

import java.awt.*;
import java.util.*;

public class ContactCellRenderer extends DefaultListCellRenderer {
	
	final static String IMAGE_PATH = "/sneer/kernel/gui/contacts/images/";

	final static ImageIcon ONLINE_ICON = new ImageIcon(ContactCellRenderer.class.getResource(IMAGE_PATH + "operator.gif"));

	final static ImageIcon OFFLINE_ICON = new ImageIcon(ContactCellRenderer.class.getResource(IMAGE_PATH + "operator_disabled.gif"));

	final static ImageIcon ERROR_ICON = new ImageIcon(ContactCellRenderer.class.getResource(IMAGE_PATH + "redled.gif"));

	final static ImageIcon UNCONFIRMED_ICON = new ImageIcon(ContactCellRenderer.class.getResource(IMAGE_PATH + "yellowled.gif"));

	final static ImageIcon CONFIRMED_ICON = new ImageIcon(ContactCellRenderer.class.getResource(IMAGE_PATH + "greenled.gif"));
	
	final static Color selected = new Color(230,240,255);

	@Override
	public Component getListCellRendererComponent(JList list, Object contactObject, int index, boolean isSelected, boolean hasFocus) {
		ContactAttributes contact = (ContactAttributes) contactObject;

		ImageIcon stateIcon = stateIconFor(contact);

		ImageIcon onlineIcon = contact.isOnline().currentValue()
			? ONLINE_ICON
			: OFFLINE_ICON;

		FlowLayout layout = new FlowLayout();
		JPanel panel = new JPanel(layout);
		layout.setAlignment(FlowLayout.LEFT);
		panel.setBackground(isSelected ? selected : Color.white);
		panel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));

		panel.add(new JLabel(onlineIcon));
		panel.add(new JLabel(stateIcon));
		panel.add(new JLabel(contact.nick().currentValue() + " - " + contact.host().currentValue() + ":" + contact.port().currentValue()));

		return panel;
	}

	private ImageIcon stateIconFor(ContactAttributes contact) {
		String state = contact.state().currentValue();
		if (state.equals(ContactAttributes.UNCONFIRMED_STATE)) return UNCONFIRMED_ICON;
		if (state.equals(ContactAttributes.CONFIRMED_STATE)) return CONFIRMED_ICON;
		return ERROR_ICON;
	}

	private static final long serialVersionUID = 1L;
}