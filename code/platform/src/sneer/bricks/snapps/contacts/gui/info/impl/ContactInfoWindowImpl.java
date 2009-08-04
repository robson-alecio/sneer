package sneer.bricks.snapps.contacts.gui.info.impl;

import static sneer.foundation.environments.Environments.my;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import sneer.bricks.hardware.gui.guithread.GuiThread;
import sneer.bricks.hardware.io.log.Logger;
import sneer.bricks.network.social.Contact;
import sneer.bricks.network.social.ContactManager;
import sneer.bricks.pulp.blinkinglights.BlinkingLights;
import sneer.bricks.pulp.blinkinglights.LightType;
import sneer.bricks.pulp.internetaddresskeeper.InternetAddress;
import sneer.bricks.pulp.internetaddresskeeper.InternetAddressKeeper;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.skin.main.synth.scroll.SynthScrolls;
import sneer.bricks.skin.widgets.reactive.LabelProvider;
import sneer.bricks.skin.widgets.reactive.ListWidget;
import sneer.bricks.skin.widgets.reactive.NotificationPolicy;
import sneer.bricks.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.bricks.skin.widgets.reactive.TextWidget;
import sneer.bricks.skin.windowboundssetter.WindowBoundsSetter;
import sneer.bricks.snapps.contacts.actions.ContactAction;
import sneer.bricks.snapps.contacts.actions.ContactActionManager;
import sneer.bricks.snapps.contacts.gui.ContactsGui;
import sneer.bricks.snapps.contacts.gui.info.ContactInfoWindow;
import sneer.foundation.lang.Functor;
import sneer.foundation.lang.PickyConsumer;
import sneer.foundation.lang.exceptions.Refusal;

class ContactInfoWindowImpl extends JFrame implements ContactInfoWindow{

	private final ContactInternetAddressList _contactAddresses = new ContactInternetAddressList();
	
	private final ListWidget<InternetAddress> _lstAddresses; {
		final Object ref[] = new Object[1];
		my(GuiThread.class).invokeAndWait(new Runnable(){ @Override public void run() {//Fix Use GUI Nature
			ref[0] = my(ReactiveWidgetFactory.class).newList(_contactAddresses.addresses(), 
			new LabelProvider<InternetAddress>(){
				@Override public Signal<? extends Image> imageFor(InternetAddress element) {
					return my(Signals.class).constant(null);
				}

				@Override public Signal<String> labelFor(InternetAddress element) {
					return my(Signals.class).constant(element.host()+" : "+element.port());
				}});
		}});
		_lstAddresses = (ListWidget<InternetAddress>) ref[0];
	}
	
	private final JTextField _host = new JTextField();
	private final JTextField _port = new JTextField();
	private InternetAddress _selectedAdress;
	
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
			@Override public void run() { open(); }
		}, true);
	}
	
	private void open() {
		if(!_isGuiInitialized) {
			_isGuiInitialized = true;
			initGui();
		}
		my(WindowBoundsSetter.class).setBestBounds(this, my(ContactActionManager.class).baseComponent());
		setVisible(true);
	}
	
	private void initGui() {
		setTitle("Contact Info:");

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder("Internet Adresses:"));
		JLabel labNickname = new JLabel("Nickname:");
		JLabel labPort = new JLabel("Port:");
		JLabel labHost = new JLabel("Host:");
		
		Signal<String> nickname = my(Signals.class).adaptSignal(
				my(ContactsGui.class).selectedContact(), 
				new Functor<Contact, Signal<String>>() { @Override public Signal<String> evaluate(Contact contact) {
					if(contact==null)
						return my(Signals.class).constant("");
					return contact.nickname();
				}});
		
		PickyConsumer<String> setter = new PickyConsumer<String>(){@Override public void consume(String value) throws Refusal {
			my(ContactManager.class).nicknameSetterFor(contact()).consume(value);
		}};
		
		_txtNickname = my(ReactiveWidgetFactory.class).newTextField(nickname, setter, NotificationPolicy.OnEnterPressedOrLostFocus);
		
		JScrollPane scroll = my(SynthScrolls.class).create();
		scroll.getViewport().add(addresses());

		JButton btnNew = new JButton("New");
		btnNew.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent e) {
			newInternetAddress();
		}});
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent e) {
			saveInternetAddress();
		}});
		
		JButton btnDel = new JButton("Delete");
		btnDel.addActionListener(new ActionListener(){ @Override public void actionPerformed(ActionEvent e) {
			delInternetAddress();
		}});
		
		setGridBagLayout(panel, labNickname, labPort, labHost, scroll, btnNew, btnSave, btnDel);
		addListSelectionListestener();

		this.setSize(400, 310);
	}

	private void setGridBagLayout(JPanel panel, JLabel labNickname, JLabel labPort, JLabel labHost, JScrollPane scroll, JButton btnNew, JButton btnSave, JButton btnDel) {
		getContentPane().setLayout(new GridBagLayout());
		getContentPane().add(labNickname,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5,5,0,5), 0, 0) );

		getContentPane().add(_txtNickname.getComponent(),  new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, 
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5,0,0,5), 0, 0) );
		
		getContentPane().add(panel,  new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5,5,5,5), 0, 0) );
		
		panel.setLayout(new GridBagLayout());
		panel.add(scroll,  new GridBagConstraints(0, 1, 12, 1, 1.0, 1.0, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5,5,5,5), 0, 0) );
		
		panel.add(labHost,  new GridBagConstraints(0, 2, 1,1, 0.0,0.0, 
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,5,0,5), 0, 0) );

		panel.add(_host,  new GridBagConstraints(1, 2, 7, 1, 2.0,0.0, 
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0, 0) );

		panel.add(labPort,  new GridBagConstraints(9, 2, 1,1, 0.0,0.0, 
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,5,5,5), 0, 0) );
		
		panel.add(_port,  new GridBagConstraints(10, 2, 2,1, 0.0,0.0, 
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,5), 0, 0) );
		
		panel.add(btnNew,  new GridBagConstraints(7,3, 1,1, 0.0,0.0, 
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,5,5,5), 0, 0) );

		panel.add(btnSave,  new GridBagConstraints(8,3, 3, 1, 0.0,0.0, 
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5,5,5,5), 0, 0) );

		panel.add(btnDel,  new GridBagConstraints(11,3, 1,1, 0.0,0.0, 
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,5,5,5), 0, 0) );
	}
	
	private void addListSelectionListestener() {
		addresses().getSelectionModel().addListSelectionListener(
				new ListSelectionListener(){ @Override public void valueChanged(ListSelectionEvent e) {
					InternetAddress selected = (InternetAddress) addresses().getSelectedValue();
					if(selected==null){
						cleanFields();
						return;
					}
					
					_selectedAdress = selected;
					_host.setText(_selectedAdress.host());
					_port.setText(""+_selectedAdress.port());
				}});
	}
	
	private void logPortValueException(Exception e) {
		my(BlinkingLights.class).turnOn(LightType.ERROR, "Invalid Port Value: " + _port.getText(), e.getMessage(), e, 20000);
		my(Logger.class).log("Invalid Port Value: {}", _port.getText());
		_port.requestFocus();
	}
	
	private void saveInternetAddress() {
		try {
			InternetAddress address = _selectedAdress;
			if(address == null || _host.getText().trim().length()==0) return;
			
			if(  _selectedAdress.host().equals(_host.getText())
			&& _selectedAdress.port()== Integer.parseInt(_port.getText())
			&& _selectedAdress.contact() == my(ContactsGui.class).selectedContact().currentValue()){
				_lstAddresses.clearSelection();
				return;
			}
			
			newInternetAddress();
			my(InternetAddressKeeper.class).remove(address);
			_lstAddresses.clearSelection();
		} catch (NumberFormatException e) {
			logPortValueException(e);			
		}
	}
	
	private void delInternetAddress() {
		InternetAddress address = _selectedAdress;
		if(address == null || _host.getText().trim().length()==0) return;
		
		my(InternetAddressKeeper.class).remove(address);
		_lstAddresses.clearSelection();
	}
	
	private void newInternetAddress() {
		try{
			String host = _host.getText();
			if(host==null || host.trim().length()==0) return;
			
			int port = Integer.parseInt(_port.getText());
			my(InternetAddressKeeper.class).add(contact(), host, port);
			
			_lstAddresses.clearSelection();
			cleanFields();
		} catch (NumberFormatException e) {
			logPortValueException(e);			
		}
	}
	
	private JList addresses() {
		return _lstAddresses.getMainWidget();
	}

	private void cleanFields() {
		_host.setText("");
		_port.setText("");
	}
	
	private Contact contact() {
		return my(ContactsGui.class).selectedContact().currentValue();
	}

}