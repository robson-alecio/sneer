package main;

import javax.swing.SwingUtilities;

import snapps.contacts.ContactsSnapp;
import snapps.owner.OwnerSnapp;
import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.pulp.connection.SocketOriginator;
import sneer.pulp.connection.SocketReceiver;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.ContactManager;
import sneer.pulp.internetaddresskeeper.InternetAddressKeeper;
import sneer.pulp.port.PortKeeper;
import sneer.skin.dashboard.Dashboard;

public class MainDemo {

	static int y = 10;

	public static void main(String[] args) throws Exception {

		Container container = ContainerUtils.newContainer();

		initNetworkDaemons(container);

		container.produce(PortKeeper.class).portSetter().consume(7777);

		addContact(container, "Sandro", "localhost");
		addContact(container, "Klaus", "klaus.selfip.net");
		
		initGui(container);
	}

	private static void initNetworkDaemons(Container container) {
		container.produce(SocketOriginator.class);
		container.produce(SocketReceiver.class);
	}

	private static void addContact(Container container, String nick, String host) {
		Contact contact = container.produce(ContactManager.class).addContact(nick);
		container.produce(InternetAddressKeeper.class).add(contact, host, 7777);
	}

	private static void initGui(Container container) throws Exception {
		container.produce(OwnerSnapp.class);
		container.produce(ContactsSnapp.class);
		container.produce(Dashboard.class);
		
		waitUntilTheGuiThreadStarts();
	}

	private static void waitUntilTheGuiThreadStarts()	throws Exception {
		SwingUtilities.invokeAndWait(new Runnable(){@Override public void run() {}});
	}
}
