package snapps.contacts.gui.impl;

import static sneer.commons.environments.Environments.my;

import java.awt.Color;
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

import sneer.pulp.contacts.Contact;
import sneer.pulp.internetaddresskeeper.InternetAddress;
import sneer.pulp.internetaddresskeeper.InternetAddressKeeper;

class InternetAddressFrame extends JFrame{

	private final Contact _contact;
	private final InternetAddressKeeper _addressKeeper = my(InternetAddressKeeper.class);
	
	private final JList _lstAddresses = new JList();
	private final JTextField _host = new JTextField();
	private final JTextField _port = new JTextField();
	
	public InternetAddressFrame(Contact contact){
		_contact = contact;
		initGui(contact);
		loadInternetAddressesForCurrentContact();
	}

	private void initGui(Contact contact) {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		setTitle(contact.nickname() + "'s  Internet Addresses:");
		
		getContentPane().setLayout(new GridBagLayout());
		getContentPane().setBackground(Color.WHITE);
		JScrollPane scroll = new JScrollPane();

		scroll.getViewport().add(_lstAddresses);
		scroll.setBorder(new EmptyBorder(2,2,2,2));
		getContentPane().add(scroll,  new GridBagConstraints(0,0, 4,1, 1.0,1.0, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0) );
		
		_host.setBorder(new TitledBorder("host"));
		getContentPane().add(_host,  new GridBagConstraints(0,1, 1,1, 3.0,0.0, 
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0, 0) );

		_port.setBorder(new TitledBorder("port"));
		getContentPane().add(_port,  new GridBagConstraints(1,1, 1,1, 1.0,0.0, 
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0, 0) );
		
		JButton btnAdd = new JButton("+");
		getContentPane().add(btnAdd,  new GridBagConstraints(2,1, 1,1, 0.0,0.0, 
				GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(8,2,2,2), 0, 0) );
		btnAdd.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent e) {
			newInternetAddress();
		}});
		
		JButton btnDel = new JButton("-");
		getContentPane().add(btnDel,  new GridBagConstraints(3,1, 1,1, 0.0,0.0, 
				GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(8,2,2,2), 0, 0) );
		btnDel.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent e) {
			delInternetAddress();
		}});
	}
	
	public void delInternetAddress() {
		throw new sneer.commons.lang.exceptions.NotImplementedYet(); // Implement
	}
	
	private void newInternetAddress() {
		String host = _host.getText();
		if(host==null || host.trim().length()==0) return;
		
		int port = Integer.parseInt(_port.getText());
		_addressKeeper.add(_contact, host, port);
		loadInternetAddressesForCurrentContact() ;
	}
	
	private void loadInternetAddressesForCurrentContact() {
		DefaultListModel model = new DefaultListModel();
		List<InternetAddress> elements = _addressKeeper.addresses().currentElements();
		for (InternetAddress internetAddress : elements) {
			if(internetAddress.contact()==_contact)
				model.addElement(internetAddress);
		}
		_lstAddresses.setModel(model);
	}
}