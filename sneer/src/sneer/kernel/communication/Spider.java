package sneer.kernel.communication;

import java.io.IOException;

import sneer.kernel.business.Business;
import sneer.kernel.business.contacts.Contact;
import wheel.io.network.OldNetwork;
import wheel.lang.Threads;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.SimpleListReceiver;

class Spider {

	static void start(OldNetwork network, ListSignal<Contact> contacts) {
		new Spider(network, contacts);
	}
	
	private Spider(OldNetwork network, ListSignal<Contact> contacts) {
		_network = network;
		_contacts = contacts;
		_contacts.addListReceiver(new MyContactReceiver());
	}
	
	private final OldNetwork _network;
	private ListSignal<Contact> _contacts;

	private class MyContactReceiver extends SimpleListReceiver {

		@Override
		public void elementAdded(int index) {
			Contact contact = _contacts.currentGet(index);
			tryToReachAssynchronously(contact);
		}


		@Override
		public void elementRemoved(int index) {
			// Implement Auto-generated method stub
			
		}

	}

	private void tryToReachAssynchronously(final Contact contact) {
		Threads.sleepWithoutInterruptions(3000); //Fix: Remove this pause. It is here just for testing.
		
		Threads.startDaemon(new Runnable(){
			@Override
			public void run() {
				tryToReach(contact);
			}
		});
	}

	private void tryToReach(Contact contact) {
		String nick = contact.nick().currentValue();
		System.out.println(nick);
		String status = isOnline(contact)
			? "ONLINE"
			: "OFFLINE";
		System.out.println(nick + " " + status);
	}

	private boolean isOnline(Contact contact) {
		String host = contact.host().currentValue();
		int port = contact.port().currentValue();
		try {
			_network.openSocket(host, port);
		} catch (IOException e) {
			System.out.println(e);
			return false;
		}
		return true;
	}
	
}
