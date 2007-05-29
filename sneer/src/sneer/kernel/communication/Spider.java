package sneer.kernel.communication;

import java.io.IOException;

import sneer.kernel.business.contacts.Contact;
import sneer.kernel.business.contacts.OnlineEvent;
import wheel.io.network.OldNetwork;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.SimpleListReceiver;

class Spider {

	static void start(OldNetwork network, ListSignal<Contact> contacts, Omnivore<OnlineEvent> onlineSetter) {
		new Spider(network, contacts, onlineSetter);
	}
	
	private Spider(OldNetwork network, ListSignal<Contact> contacts,  Omnivore<OnlineEvent> onlineSetter) {
		_network = network;
		_contacts = contacts;
		_onlineSetter = onlineSetter;
		
		_contacts.addListReceiver(new MyContactReceiver());
	}
	
	private final OldNetwork _network;
	private ListSignal<Contact> _contacts;
	private final Omnivore<OnlineEvent> _onlineSetter;

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
		Threads.startDaemon(new Runnable(){
			@Override
			public void run() {
				while (true) {
					tryToReach(contact);
					Threads.sleepWithoutInterruptions(5000);
				}
			}
		});
	}

	private void tryToReach(Contact contact) {
		boolean isOnline = isOnline(contact);

		Boolean wasOnline = contact.isOnline().currentValue();
		if (isOnline == wasOnline) return;
		
		String nick = contact.nick().currentValue();
		_onlineSetter.consume(new OnlineEvent(nick, isOnline));
	}

	private boolean isOnline(Contact contact) {
		String host = contact.host().currentValue();
		int port = contact.port().currentValue();
		try {
			_network.openSocket(host, port);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
}
