package snapps.contacts.gui.info.impl;

import static sneer.commons.environments.Environments.my;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import snapps.contacts.actions.ContactAction;
import snapps.contacts.actions.ContactActionManager;
import snapps.contacts.gui.ContactsGui;
import snapps.contacts.gui.info.ContactInfoWindow;
import sneer.commons.lang.Functor;
import sneer.hardware.cpu.exceptions.IllegalParameter;
import sneer.hardware.cpu.lang.Consumer;
import sneer.hardware.cpu.lang.PickyConsumer;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.ContactManager;
import sneer.pulp.internetaddresskeeper.InternetAddress;
import sneer.pulp.internetaddresskeeper.InternetAddressKeeper;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.Signals;
import sneer.skin.colors.Colors;
import sneer.skin.widgets.reactive.NotificationPolicy;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.skin.widgets.reactive.TextWidget;
import sneer.skin.windowboundssetter.WindowBoundsSetter;

class ContactInfoWindowImpl extends JFrame implements ContactInfoWindow{

	private final InternetAddressKeeper _addressKeeper = my(InternetAddressKeeper.class);
	
	private final JList _lstAddresses = new JList();
	private final JTextField _host = new JTextField();
	private final JTextField _port = new JTextField();
	
	private boolean _isGuiInitialized = false;
	private TextWidget<JTextField> _txtNickname;

	ContactInfoWindowImpl() {
		addContactEditAction();
	}

	private void addContactEditAction() {
		my(ContactActionManager.class).addContactAction(new ContactAction(){
			@Override public boolean isEnabled() { return true; }
			@Override public boolean isVisible() { return true; }
			@Override public String caption() { return "Edit Contact...";}
			@Override public void run() {
				my(ContactInfoWindow.class).open();
		}}, true);
	}
	
	@Override
	public void open() {
		if(!_isGuiInitialized) {
			_isGuiInitialized = true;
			initGui();
			loadInternetAddressesForCurrentContact();
		}
		setVisible(true);
	}
	
	private void rebindContact() {
		if (!_isGuiInitialized) return;
		loadInternetAddressesForCurrentContact();
	}

	private void initGui() {
		setTitle("Contact Info:");
		Signal<String> nickname = my(Signals.class).adaptSignal(my(ContactsGui.class).selectedContact(), new Functor<Contact, Signal<String>>() { @Override public Signal<String> evaluate(Contact contact) {
			return contact.nickname();
		}});
		
		PickyConsumer<String> setter = new PickyConsumer<String>(){@Override public void consume(String value) throws IllegalParameter {
			my(ContactManager.class).nicknameSetterFor(contact()).consume(value);
		}};
		
		getContentPane().setLayout(new GridBagLayout());
		
		_txtNickname = my(ReactiveWidgetFactory.class).newTextField(nickname, setter, NotificationPolicy.OnEnterPressedOrLostFocus);
		JLabel _labNickname = new JLabel("Nickname:");
		
		getContentPane().add(_labNickname,  new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5,5,0,5), 0, 0) );

		getContentPane().add(_txtNickname.getComponent(),  new GridBagConstraints(3, 0, 10, 1, 1.0, 0.0, 
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5,0,0,5), 0, 0) );
		
		JScrollPane scroll = new JScrollPane();
		scroll.getViewport().add(_lstAddresses);
		scroll.setBorder(new EmptyBorder(0,0,0,0));
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder("Internet Adresses:"));
		panel.setLayout(new BorderLayout());
		panel.add(scroll, BorderLayout.CENTER);
		panel.setBackground(my(Colors.class).solid());
		
		getContentPane().add(panel,  new GridBagConstraints(0, 1, 12, 1, 1.0, 1.0, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5,5,5,5), 0, 0) );
		
		JLabel _labHost = new JLabel("Host:");
		getContentPane().add(_labHost,  new GridBagConstraints(0, 2, 1,1, 0.0,0.0, 
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,5,0,5), 0, 0) );

		getContentPane().add(_host,  new GridBagConstraints(1, 2, 7, 1, 2.0,0.0, 
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0, 0) );

		JLabel _labPort = new JLabel("Port:");
		getContentPane().add(_labPort,  new GridBagConstraints(9, 2, 1,1, 0.0,0.0, 
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,5,5,5), 0, 0) );
		
		getContentPane().add(_port,  new GridBagConstraints(10, 2, 2,1, 0.0,0.0, 
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,5), 0, 0) );
		
		JButton btnNew = new JButton("New");
		getContentPane().add(btnNew,  new GridBagConstraints(7,3, 1,1, 0.0,0.0, 
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,5,5,5), 0, 0) );
		btnNew.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent e) {
			newInternetAddress();
		}});

		JButton btnSave = new JButton("Save");
		getContentPane().add(btnSave,  new GridBagConstraints(8,3, 3, 1, 0.0,0.0, 
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5,5,5,5), 0, 0) );
		btnSave.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent e) {
			saveInternetAddress();
		}});
		
		JButton btnDel = new JButton("Delete");
		getContentPane().add(btnDel,  new GridBagConstraints(11,3, 1,1, 0.0,0.0, 
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,5,5,5), 0, 0) );
		btnDel.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent e) {
			delInternetAddress();
		}});
		
		listenToSignals();
		addListSelectionListestener();

		this.setSize(400, 350);
		my(WindowBoundsSetter.class).setBestBounds(this);
	}
	
	private void saveInternetAddress() {
		throw new sneer.commons.lang.exceptions.NotImplementedYet(); // Implement
	}
	
	private void listenToSignals() {
		my(Signals.class).receive(this, new Consumer<Contact>(){ @Override public void consume(Contact value) {
			rebindContact();
		}}, my(ContactsGui.class).selectedContact());
	}

	private void addListSelectionListestener() {
		_lstAddresses.getSelectionModel().addListSelectionListener(
				new ListSelectionListener(){ @Override public void valueChanged(ListSelectionEvent e) {
					InternetAddressPrettyPrinter wrapper = (InternetAddressPrettyPrinter) _lstAddresses.getSelectedValue();
					if(wrapper==null){
						cleanFields();
						return;
					}
					
					InternetAddress address = wrapper._address;
					_host.setText(address.host());
					_port.setText(""+address.port());
				}});
	}
	
	private void delInternetAddress() {
		InternetAddressPrettyPrinter wrapper = (InternetAddressPrettyPrinter) _lstAddresses.getSelectedValue();
		if(wrapper==null) return;
		
		InternetAddress address = wrapper._address;
		_addressKeeper.remove(address);
		((DefaultListModel)_lstAddresses.getModel()).removeElement(wrapper);
	}
	
	private void newInternetAddress() {
		String host = _host.getText();
		if(host==null || host.trim().length()==0) return;
		
		int port = Integer.parseInt(_port.getText());
		_addressKeeper.add(contact(), host, port);
		loadInternetAddressesForCurrentContact() ;
		cleanFields();
	}

	private void cleanFields() {
		_host.setText("");
		_port.setText("");
	}
	
	private void loadInternetAddressesForCurrentContact() {
		DefaultListModel model = new DefaultListModel();
		List<InternetAddress> elements = _addressKeeper.addresses().currentElements();
		for (InternetAddress internetAddress : elements) {
			if(internetAddress.contact() == contact())
				model.addElement(new InternetAddressPrettyPrinter(internetAddress));
		}
		_lstAddresses.setModel(model);
	}
	
	private Contact contact() {
		return my(ContactsGui.class).selectedContact().currentValue();
	}
	
	private class InternetAddressPrettyPrinter{
		final InternetAddress _address;
		
		public InternetAddressPrettyPrinter(InternetAddress address) {
			_address = address;
		}
		
		@Override 
		public String toString() {
			return _address.host()+" : "+_address.port();
		}
	}
}