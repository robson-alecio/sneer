package snapps.contacts.tests;

import snapps.contacts.ContactsSnapp;
import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.pulp.contacts.ContactManager;
import sneer.skin.dashboard.Dashboard;

public class ContactsSnappDemo  {
	
	static int y = 10;

	public static void main(String[] args) throws Exception {

		Container container = ContainerUtils.getContainer();

		Dashboard dashboard = container.produce(Dashboard.class);
		ContactsSnapp snapp = container.produce(ContactsSnapp.class);
		dashboard.installSnapp(snapp);
		
		ContactManager manager = container.produce(ContactManager.class);
		manager.addContact("Sandro");
		manager.addContact("Klaus");
		manager.addContact("Nell");
		manager.addContact("Bamboo");
	}
}