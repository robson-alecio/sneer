package sneer.kernel.gui.contacts;

import static wheel.i18n.Language.translate;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import sneer.kernel.pointofview.Contact;

public class DropChooser extends JFrame{

	private final Contact _contact;
	private final Object _object;
	private DefaultListModel _listModel = new DefaultListModel();
	private JList _list;
	private JButton _chooseItButton;

	public DropChooser(List<DropAction> interested, Contact contact, Object object) {
		_contact = contact;
		_object = object;

		for(DropAction dropAction:interested)
			_listModel.addElement(new ListItem(dropAction));

		initComponents();
	}

	private void initComponents() {
		setTitle(translate("Interested Applications"));
		setLayout(new BorderLayout());
		_list = new JList(_listModel );
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));
		_chooseItButton = new JButton(translate("Choose It"));
		_chooseItButton.setMaximumSize(new Dimension(150, 30));
		_chooseItButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		_chooseItButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				chooseIt(_list.getSelectedValues());
			}
		});
		buttonPanel.add(_chooseItButton);
		
		add(new JScrollPane(_list),BorderLayout.CENTER);
		add(buttonPanel,BorderLayout.EAST);
		setSize(300,400);
		setVisible(true);
	}
	
	private void chooseIt(Object[] selectedValues) {
		for(Object selected:selectedValues){
			ListItem item = (ListItem)selected;
			item._dropAction.actUpon(_contact, _object);
		}
		setVisible(false);
	}

	private class ListItem{
		private final DropAction _dropAction;

		ListItem(DropAction dropAction){
			_dropAction = dropAction;
		}
		
		@Override
		public String toString(){
			return _dropAction.caption();
		}
	}

	private static final long serialVersionUID = 1L;
}
