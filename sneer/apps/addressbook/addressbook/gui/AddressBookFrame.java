package addressbook.gui;

import static wheel.i18n.Language.translate;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import sneer.kernel.api.SovereignApplicationNeeds;
import wheel.lang.Pair;
import addressbook.business.AddressBookSource;
import addressbook.business.Person;
import addressbook.business.PersonInfo;

public class AddressBookFrame extends JFrame{

	private static final int LABELWIDTH = 60;
	private JButton _addButton = new JButton("Add");
	private JButton _editButton = new JButton("Edit");
	private JButton _removeButton = new JButton("Remove");
	
	private CardLayout _switcher = new CardLayout();
	
	private DefaultListModel _model = new DefaultListModel();
	private JList _list = new JList(_model);
	private AddressBookSource _addressBookSource;
	
	public AddressBookFrame(SovereignApplicationNeeds config) {
		_addressBookSource = (AddressBookSource)config.prevalentState();
		Rectangle bounds = _addressBookSource.output().bounds().currentValue();
		initComponents();
		updateList();
		initBoundsKeeper(bounds);
	}

	private void initBoundsKeeper(Rectangle bounds) {
		setLocation(bounds.x, bounds.y);
		setSize(bounds.width,bounds.height);
		addComponentListener(new ComponentAdapter() {
			
			@Override
			public void componentResized(ComponentEvent e) {
				boundsChanged();
			}

			private void boundsChanged() {
				if (!AddressBookFrame.this.isVisible())
					return;
				Rectangle frameBounds = AddressBookFrame.this.getBounds();
				if (_addressBookSource.output().bounds().currentValue().equals(frameBounds)) return;
					_addressBookSource.boundsSetter().consume(frameBounds);
			}
		
			@Override
			public void componentMoved(ComponentEvent e) {
				boundsChanged();
			}
		
		});
	}

	private JPanel _mainPanel = new JPanel(new BorderLayout());
	
	private void initComponents() {
		setLayout(new BorderLayout());
		prepareList();
		_mainPanel.setLayout(_switcher);
		_mainPanel.add(listPanel(),"listPanel");
		_mainPanel.add(addOrUpdatePanel(),"addOrUpdatePanel");
		add(_mainPanel,BorderLayout.CENTER);
	}

	private void prepareList() {
		_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_list.setCellRenderer(new AddressBookListCellRenderer());
		_list.setBackground(Color.black);
		_list.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e) {
				Person person = (Person)_list.getSelectedValue();
				_editButton.setEnabled((person!=null));
				_removeButton.setEnabled((person!=null));
			}
			
		});
	}
	
	private JTextField _nameField = new JTextField();
	private JTextField _addressField = new JTextField();
	private JTextField _phoneField = new JTextField();
	private JTextField _emailField = new JTextField();
	
	private JButton _saveButton = new JButton("Save");
	private JButton _cancelButton = new JButton("Cancel");
	private Person _current = null;
	
	private JPanel addOrUpdatePanel() {
		JPanel panel = new JPanel(new BorderLayout());
		JPanel fields = new JPanel();
		fields.setLayout(new BoxLayout(fields,BoxLayout.Y_AXIS));
		fields.add(field("Name:",_nameField,LABELWIDTH));
		fields.add(field("Address:",_addressField,LABELWIDTH));
		fields.add(field("Phone:",_phoneField,LABELWIDTH));
		fields.add(field("Email:",_emailField,LABELWIDTH));
		JPanel buttons = new JPanel(new BorderLayout());
		buttons.add(_saveButton,BorderLayout.EAST);
		buttons.add(_cancelButton,BorderLayout.WEST);
		panel.add(fields,BorderLayout.NORTH);
		panel.add(buttons,BorderLayout.SOUTH);
		_saveButton.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e) {
			if (_nameField.getText().trim().length()==0){
				JOptionPane.showMessageDialog(null, translate("Please fill the name field!"));
				return;
			}
				
			PersonInfo person = new PersonInfo(_nameField.getText(),_addressField.getText(),_phoneField.getText(),_emailField.getText());
			if (_current==null){			
				try{
					_addressBookSource.personAdder().consume(person);
				}catch(Exception ex){
					JOptionPane.showMessageDialog(null, translate("Name already Exists!"));
					return;
				}
			}else{
				PersonInfo old = new PersonInfo(_current.name().currentValue(),null,null,null);
				try{
					_addressBookSource.personUpdater().consume(new Pair<PersonInfo,PersonInfo>(old,person));
				}catch(Exception ex){
					JOptionPane.showMessageDialog(null, translate("Name already Exists!"));
					return;
				}
			}
			updateList();
			_switcher.show(_mainPanel,"listPanel");
		}});
		_cancelButton.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e) {
			_switcher.show(_mainPanel,"listPanel");
		}});
		return panel;
	}
	
	private void updateFields(){
		if (_current==null){
			_nameField.setText("");
			_addressField.setText("");
			_phoneField.setText("");
			_emailField.setText("");
		}else{
			_nameField.setText(_current.name().currentValue());
			_addressField.setText(_current.address().currentValue());
			_phoneField.setText(_current.phone().currentValue());
			_emailField.setText(_current.email().currentValue());
		}
	}

	private JPanel listPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));
		_addButton = createButton(translate("Add"),new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				_current = null;
				updateFields();
				_switcher.show(_mainPanel,"addOrUpdatePanel");
			}
		});
		_editButton = createButton(translate("Edit"),new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				_current = (Person)_list.getSelectedValue();
				if (_current==null)
					return;
				updateFields();
				_switcher.show(_mainPanel,"addOrUpdatePanel");
			}
		});
		_removeButton = createButton(translate("Remove"),new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				_current = (Person)_list.getSelectedValue();
				if (_current!=null){
					PersonInfo person = new PersonInfo(_current.name().currentValue(),null,null,null);
					_addressBookSource.personRemover().consume(person);
				}
				updateList();
			}
		});
		buttonPanel.add(_addButton);
		buttonPanel.add(_editButton);
		buttonPanel.add(_removeButton);
		panel.add(new JScrollPane(_list),BorderLayout.CENTER);
		panel.add(buttonPanel,BorderLayout.EAST);
		_editButton.setEnabled(false);
		_removeButton.setEnabled(false);
		return panel;
	}
	
	private void updateList(){
		_model.clear();
		List<Person> orderedList = new ArrayList<Person>();
		for(Person person:_addressBookSource.output().personList())
			orderedList.add(person);
		Collections.sort(orderedList);
		for(Person person:orderedList)
			_model.add(0, person);
	}

	private JButton createButton(String label, ActionListener listener){
		JButton button = new JButton(label);
		button.setMaximumSize(new Dimension(150, 30));
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.addActionListener(listener);
		return button;
	}
	private JPanel field(String label, JTextField textField, int labelWidth){
		JPanel panel = new JPanel(new BorderLayout());
		JLabel jlabel = new JLabel(label);
		jlabel.setPreferredSize(new Dimension(labelWidth,25));
		panel.add(jlabel,BorderLayout.WEST);
		panel.add(textField,BorderLayout.CENTER);
		return panel;
	}
	
	
	private static final long serialVersionUID = 1L;
	
}
