package snapps.contacts.gui.impl;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import snapps.contacts.gui.impl.ContactsGuiImpl.ContactLabelProvider;
import sneer.pulp.contacts.list.ContactInfo;
import wheel.reactive.Signal;

class ContactsGuiCellRenderer implements ListCellRenderer {

	private final DefaultListCellRenderer renderer = new DefaultListCellRenderer();
	private final ContactLabelProvider _labelProvider;

	ContactsGuiCellRenderer(ContactLabelProvider labelProvider) {
		_labelProvider = labelProvider;
	}
	
	@Override
	public Component getListCellRendererComponent(JList ignored, Object value, int ignored2, boolean isSelected, boolean cellHasFocus) {

		ContactInfo contactInfo = getElement(value);
		
		Signal<String> slabel = _labelProvider.labelFor(contactInfo);
		Signal<Image> sicon = _labelProvider.imageFor(contactInfo);

		ImageIcon icon = new ImageIcon(sicon.currentValue());
		JLabel label = (JLabel) renderer.getListCellRendererComponent(ignored, value, ignored2, isSelected, cellHasFocus);
		label.setIcon(icon);
		label.setText(slabel.currentValue());
		
		if(!contactInfo.isOnline().currentValue())
			label.setForeground(Color.LIGHT_GRAY);

		return label;
	}

	protected ContactInfo getElement(Object value) {
		return (ContactInfo) value;
	}
}