package addressbook.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import addressbook.business.Person;

public class AddressBookListCellRenderer implements ListCellRenderer{

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		if (value==null)
			return null;
		JPanel panel = new JPanel();
		if (isSelected)
			panel.setBackground(Color.yellow);
		else
			panel.setBackground(Color.white);
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		Border border = new CompoundBorder(new LineBorder(Color.BLACK,1),new EmptyBorder(5,5,5,5));
		panel.setBorder(border);
		Person person = (Person) value;
		JLabel name = new JLabel(" "+person.name().currentValue());
		name.setForeground(Color.red);
		panel.add(name);
		panel.add(new JLabel(" "+person.address().currentValue()));
		panel.add(new JLabel(" "+person.phone().currentValue()));
		panel.add(new JLabel(" "+person.email().currentValue()));
		return panel;
	}

}
