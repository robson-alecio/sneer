package snapps.contacts.tests;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.border.TitledBorder;

import snapps.contacts.ContactsSnapp;
import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.ContactManager;
import sneer.skin.dashboard.Dashboard;
import sneer.skin.snappmanager.Instrument;
import sneer.skin.snappmanager.InstrumentManager;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;

public class ContactsSnappDemo  {
	
	public static void main(String[] args) throws Exception {

		Container container = ContainerUtils.getContainer();

		container.produce(Dashboard.class);
		container.produce(ContactsSnapp.class);
		
		ReactiveWidgetFactory rfactory = container.produce(ReactiveWidgetFactory.class);

		ContactManager manager = container.produce(ContactManager.class);
		
 
		manager.addContact("Sandro");
		manager.addContact("Klaus");
		manager.addContact("Bamboo");
		Contact contact = manager.addContact("Nell");
		
		InstrumentManager snapps = container.produce(InstrumentManager.class);
		new NicknameDemo(rfactory, snapps, contact, manager);
	}
}

class NicknameDemo implements Instrument{

	private final ReactiveWidgetFactory _rfactory;
	private final InstrumentManager _snapps;
	private final Contact _test;
	private final ContactManager _manager;

	public NicknameDemo(ReactiveWidgetFactory rfactory, InstrumentManager snapps, Contact test, ContactManager manager) {
		_rfactory = rfactory;
		_snapps = snapps;
		_test = test;
		_manager = manager;
		_snapps.registerInstrument(this);
	}

	@Override
	public void init(java.awt.Container container) {
		container.setLayout(new BorderLayout());
		JComponent field = _rfactory.newEditableLabel(_test.nickname(),_manager.nicknameSetterFor(_test)).getComponent();
		container.add(field);
		field.setBorder(new TitledBorder("Change Nickname Here:"));
	}
	
}