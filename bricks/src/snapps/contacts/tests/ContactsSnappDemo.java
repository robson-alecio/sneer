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
import sneer.skin.snappmanager.SnappManager;
import sneer.skin.viewmanager.Snapp;
import sneer.skin.widgets.reactive.RFactory;

public class ContactsSnappDemo  {
	
	static int y = 10;

	public static void main(String[] args) throws Exception {

		Container container = ContainerUtils.getContainer();

		container.produce(Dashboard.class);
		container.produce(ContactsSnapp.class);
		
		RFactory rfactory = container.produce(RFactory.class);

		ContactManager manager = container.produce(ContactManager.class);
		
 
		manager.addContact("Sandro");
		manager.addContact("Klaus");
		manager.addContact("Bamboo");
		Contact contact = manager.addContact("Nell");
		
		SnappManager snapps = container.produce(SnappManager.class);
		new NicknameDemo(rfactory, snapps, contact, manager);
	}
}

class NicknameDemo implements Snapp{

	private final RFactory _rfactory;
	private final SnappManager _snapps;
	private final Contact _test;
	private final ContactManager _manager;

	public NicknameDemo(RFactory rfactory, SnappManager snapps, Contact test, ContactManager manager) {
		_rfactory = rfactory;
		_snapps = snapps;
		_test = test;
		_manager = manager;
		_snapps.registerSnapp(this);
	}

	@Override
	public void init(java.awt.Container container) {
		container.setLayout(new BorderLayout());
		JComponent field = _rfactory.newEditableLabel(_test.nickname(),_manager.nicknameSetterFor(_test)).getComponent();
		container.add(field);
		field.setBorder(new TitledBorder("Change Nickname Here:"));
	}
	@Override
	public String getName() {
		return "Output Test";
	}
}