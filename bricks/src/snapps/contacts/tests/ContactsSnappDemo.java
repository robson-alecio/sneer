package snapps.contacts.tests;

import snapps.contacts.ContactsSnapp;
import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.kernel.container.Inject;
import sneer.pulp.contacts.ContactManager;
import sneer.skin.dashboard.Dashboard;
import sneer.skin.snappmanager.SnappManager;
import sneer.skin.viewmanager.Snapp;
import sneer.skin.widgets.reactive.RFactory;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public class ContactsSnappDemo  {
	
	static int y = 10;

	public static void main(String[] args) throws Exception {

		Container container = ContainerUtils.getContainer();

		container.produce(Dashboard.class);
		container.produce(ContactsSnapp.class);
		
//		RFactory rfactory = container.produce(RFactory.class);

		ContactManager manager = container.produce(ContactManager.class);
		
		manager.addContact("Sandro");
		manager.addContact("Sandro1");
		manager.addContact("Sandro2");
		manager.addContact("Sandro3");
//		Contact contact = manager.contactGiven("Sandro");
//		Register<Contact> register = new RegisterImpl<Contact>(contact);
//		SnappTest test = new SnappTest(rfactory, register.output(), register.setter());
		
		
	}
}
class SnappTest implements Snapp{

	private final RFactory _rfactory;
	private final Signal<String> _output;
	private final Omnivore<String> _setter;
	
	@Inject
	static private SnappManager _snapps;

	public SnappTest(RFactory rfactory, Signal<String> output,	Omnivore<String> setter) {
		_rfactory = rfactory;
		_output = output;
		_setter = setter;
		_snapps.registerSnapp(this);
	} 

	@Override
	public void init(java.awt.Container container) {
		container.add(
			_rfactory.newEditableLabel(_output, _setter, true).getComponent()
		);
	}
	@Override
	public String getName() {
		return "Output Test";
	}
}