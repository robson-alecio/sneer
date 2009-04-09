package snapps.contacts.internetaddress.gui.impl;

import static sneer.commons.environments.Environments.my;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import snapps.contacts.internetaddress.gui.InternetAddressWindow;
import sneer.pulp.contacts.Contact;
import sneer.pulp.internetaddresskeeper.InternetAddress;
import sneer.pulp.internetaddresskeeper.InternetAddressKeeper;
import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.impl.RegisterImpl;
import sneer.skin.colors.Colors;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.skin.widgets.reactive.TextWidget;
import sneer.skin.windowboundssetter.WindowBoundsSetter;
import wheel.lang.Consumer;

class InternetAddressWindowImpl extends JFrame implements InternetAddressWindow{

	private final InternetAddressKeeper _addressKeeper = my(InternetAddressKeeper.class);
	
	private final JList _lstAddresses = new JList();
	private final JTextField _host = new JTextField();
	private final JTextField _port = new JTextField();
	
	private boolean _isGuiInitialized = false;
	private Contact _contact;
	private TextWidget<JTextField> _txtNickname;
	
	private final Register<String> _contactProxy = new RegisterImpl<String>(null);
	private Consumer<String> adapterToVoidGC;
	
	@Override
	public void setActiveContact(Contact contact){
		_contact = contact;
		if(!_isGuiInitialized) initGui();
		rebindContact();
	}

	@Override
	public void open() {
		if(_contact == null) return;
		setVisible(true);
	}

	//Refactor add a better rebind support
	private void rebindContact() {
		_contactProxy.setter().consume(_contact.nickname().currentValue());
		adapterToVoidGC = new Consumer<String>(){ @Override public void consume(String value) {
			_contact.nicknameSetter().consume(value);
		}};
		_contactProxy.output().addReceiver(adapterToVoidGC);
		loadInternetAddressesForCurrentContact();
	}

	private void initGui() {
		
		_txtNickname = my(ReactiveWidgetFactory.class).newTextField(_contactProxy.output(), _contactProxy.setter());
		setTitle(_contact.nickname() + "'s  Internet Addresses:");
		loadInternetAddressesForCurrentContact();
		
		getContentPane().setLayout(new GridBagLayout());
		getContentPane().setBackground(my(Colors.class).solid());
		JScrollPane scroll = new JScrollPane();

		scroll.getViewport().add(_lstAddresses);
		scroll.setBorder(new EmptyBorder(2,2,2,2));
		
		getContentPane().add(_txtNickname.getComponent(),  new GridBagConstraints(0,0, 4, 1, 1.0, 0.0, 
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0, 0) );
		
		getContentPane().add(scroll,  new GridBagConstraints(0,1, 4,1, 1.0,1.0, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0) );
		
		_host.setBorder(new TitledBorder("host"));
		getContentPane().add(_host,  new GridBagConstraints(0,2, 1,1, 3.0,0.0, 
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0, 0) );

		_port.setBorder(new TitledBorder("port"));
		getContentPane().add(_port,  new GridBagConstraints(1,2, 1,1, 1.0,0.0, 
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0, 0) );
		
		JButton btnAdd = new JButton("+");
		getContentPane().add(btnAdd,  new GridBagConstraints(2,2, 1,1, 0.0,0.0, 
				GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(8,2,2,2), 0, 0) );
		btnAdd.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent e) {
			newInternetAddress();
		}});
		
		JButton btnDel = new JButton("-");
		getContentPane().add(btnDel,  new GridBagConstraints(3,2, 1,1, 0.0,0.0, 
				GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(8,2,2,2), 0, 0) );
		btnDel.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent e) {
			delInternetAddress();
		}});
		
		addListSelectionListestener();
		
		this.setSize(400, 200);
		my(WindowBoundsSetter.class).setBestBounds(this);
		_isGuiInitialized = true;
	}

	private void addListSelectionListestener() {
		_lstAddresses.getSelectionModel().addListSelectionListener(
				new ListSelectionListener(){ @Override public void valueChanged(ListSelectionEvent e) {
					InternetAddressWrapper wrapper = (InternetAddressWrapper) _lstAddresses.getSelectedValue();
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
		InternetAddressWrapper wrapper = (InternetAddressWrapper) _lstAddresses.getSelectedValue();
		if(wrapper==null) return;
		
		InternetAddress address = wrapper._address;
		_addressKeeper.remove(address);
		((DefaultListModel)_lstAddresses.getModel()).removeElement(wrapper);
	}
	
	private void newInternetAddress() {
		String host = _host.getText();
		if(host==null || host.trim().length()==0) return;
		
		int port = Integer.parseInt(_port.getText());
		_addressKeeper.add(_contact, host, port);
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
			if(internetAddress.contact()==_contact)
				model.addElement(new InternetAddressWrapper(internetAddress));
		}
		_lstAddresses.setModel(model);
	}
	
	private class InternetAddressWrapper{
		final InternetAddress _address;
		
		public InternetAddressWrapper(InternetAddress address) {
			_address = address;
		}
		
		@Override 
		public String toString() {
			return _address.host()+" : "+_address.port();
		}
	}
}