package sneer.kernel.communication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import sneer.apps.messages.ChatEvent;
import sneer.kernel.business.contacts.Contact;
import sneer.kernel.business.contacts.OnlineEvent;
import wheel.io.network.ObjectSocket;
import wheel.io.network.OldNetwork;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.SimpleListReceiver;

class Spider {

	private class MyChatEventSender extends SimpleListReceiver { //Does not extend SimpeListRecceiver. Make an inner class MyReceiver that does.

		private final Signal<String> _host;
		private final Signal<Integer> _port;
		private final ListSignal<ChatEvent> _chatEvents;
		private volatile boolean _working = false;

		public MyChatEventSender(Signal<String> host, Signal<Integer> port, ListSignal<ChatEvent> chatEvents) {
			_host = host;
			_port = port;
			_chatEvents = chatEvents;
		}

		@Override
		public void elementAdded(int index) {
			if (!_working) return;
			try {
				ObjectSocket socket = produceSocket(_host.currentValue(), _port.currentValue());
				ChatEvent chatEvent = _chatEvents.currentGet(index);
				if (chatEvent._destination == null) return;
				socket.writeObject(chatEvent);
			} catch (IOException e) {
				//Fix: deal with the lost event.
			}
		}

		public void startWorking() { //Refactor: clean this crap.
			_working = true;			
		}

	}

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
	private final ListSignal<Contact> _contacts;
	private final Omnivore<OnlineEvent> _onlineSetter;
	private final Map<String, ObjectSocket> _socketsByAddress = new HashMap<String, ObjectSocket>();

	private class MyContactReceiver extends SimpleListReceiver {

		@Override
		public void elementAdded(int index) {
			Contact contact = _contacts.currentGet(index);
			startIsOnlineWatchdog(contact);
			MyChatEventSender myChatEventSender = new MyChatEventSender(contact.host(), contact.port(), contact.chatEventsPending());
			contact.chatEventsPending().addListReceiver(myChatEventSender);
			myChatEventSender.startWorking();
		}

		@Override
		public void elementRemoved(int index) {
			// Implement Auto-generated method stub
			
		}

	}

	private void startIsOnlineWatchdog(final Contact contact) {
		Threads.startDaemon(new Runnable(){
			@Override
			public void run() {
				while (true) {
					barkTo(contact);
					Threads.sleepWithoutInterruptions(5000);
				}
			}
		});
	}

	private void barkTo(Contact contact) {
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
			produceSocket(host, port).writeObject("Bark");
			return true;
		} catch (IOException e) {
			discardSocket(host, port);
			return false;
		}
	}

	private void discardSocket(String host, int port) {
		_socketsByAddress.remove(address(host, port));
	}

	private ObjectSocket produceSocket(String host, int port) throws IOException {
		String address = address(host, port);
		ObjectSocket result = _socketsByAddress.get(address);
		
		if (result == null) {
			result = _network.openSocket(host, port);
			_socketsByAddress.put(address, result);
		}
		
		return result;
	}

	private String address(String host, int port) {
		return host + ":" + port;
	}
	
}
