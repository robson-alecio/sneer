package sneer.kernel.appmanager;

import sneer.apps.asker.Asker;
import sneer.apps.transferqueue.TransferQueue;
import sneer.kernel.api.SovereignApplicationNeeds;
import sneer.kernel.business.contacts.ContactAttributes;
import sneer.kernel.communication.Channel;
import sneer.kernel.pointofview.Contact;
import wheel.io.ui.User;
import wheel.io.ui.User.Notification;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;

public class NeedsImpl implements SovereignApplicationNeeds {
	
	private final Channel _channel;
	private final User _user;
	private final ListSignal<Contact> _contacts;
	private final Omnivore<Notification> _briefUserNotifier;
	private final ListSignal<ContactAttributes> _contactAttributes;
	private final Signal<String> _ownName;
	private final Asker _asker;
	private final TransferQueue _transfer;
	

	public NeedsImpl(User user, Channel channel, ListSignal<Contact> contacts, ListSignal<ContactAttributes> contactAttributes, Signal<String> ownName, Omnivore<Notification> briefUserNotifier, Asker asker, TransferQueue transfer) {
		_user = user;
		_channel = channel;
		_contacts = contacts;
		_contactAttributes = contactAttributes;
		_ownName = ownName;
		_briefUserNotifier = briefUserNotifier;
		_asker = asker;
		_transfer = transfer;
	}

	@Override
	public Channel channel() {
		return _channel;
	}

	@Override
	public User user() {
		return _user;
	}


	@Override
	public ListSignal<Contact> contacts() {
		return _contacts;
	}


	@Override
	public Omnivore<Notification> briefUserNotifier() {
		return _briefUserNotifier;
	}

	@Override
	public ListSignal<ContactAttributes> contactAttributes() {
		return _contactAttributes;
	}


	@Override
	public Signal<String> ownName() {
		return _ownName;
	}


	@Override
	public Asker asker() {
		return _asker;
	}


	@Override
	public TransferQueue transfer() {
		return _transfer;
	}

	@Override
	public Object prevalentState() {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

}
