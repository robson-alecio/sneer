package sneer.kernel.gui.contacts;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import sneer.kernel.pointofview.Contact;

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
		Contact contact = (Contact) contactObject;

		ImageIcon stateIcon = stateIconFor(contact);

		ImageIcon onlineIcon = contact.party().isOnline().currentValue()
			? ONLINE_ICON
			: OFFLINE_ICON;

		FlowLayout layout = new FlowLayout();
		JPanel panel = new JPanel(layout);
		layout.setAlignment(FlowLayout.LEFT);
		panel.setBackground(isSelected ? selected : Color.white);
		panel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));

		panel.add(new JLabel(onlineIcon));
		panel.add(new JLabel(stateIcon));
		panel.add(new JLabel(contact.nick().currentValue() + " - " + contact.party().host().currentValue() + ":" + contact.party().port().currentValue()));

		return panel;
	}

	private ImageIcon stateIconFor(Contact contact) {
		return contact.party().publicKeyConfirmed().currentValue()
			? CONFIRMED_ICON
			: UNCONFIRMED_ICON;
	}

	private static final long serialVersionUID = 1L;
}