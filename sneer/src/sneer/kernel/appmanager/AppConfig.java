package sneer.kernel.appmanager;

import prevayler.bubble.PrevalentBubbleBlower;
import sneer.apps.asker.Asker;
import sneer.apps.transferqueue.TransferQueue;
import sneer.kernel.business.contacts.ContactAttributes;
import sneer.kernel.communication.Channel;
import sneer.kernel.pointofview.Contact;
import wheel.io.ui.User;
import wheel.io.ui.User.Notification;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;

public class AppConfig {
	
	public final Channel _channel;
	public final User _user;
	public final ListSignal<Contact> _contacts;
	public final Omnivore<Notification> _briefUserNotifier;
	public final PrevalentBubbleBlower _prevalentBubbleBlower;
	public final ListSignal<ContactAttributes> _contactAttributes;
	public final Signal<String> _ownName;
	public final Asker _asker;
	public final TransferQueue _transfer;
	

	public AppConfig(User user, Channel channel, ListSignal<Contact> contacts, ListSignal<ContactAttributes> contactAttributes, Signal<String> ownName, Omnivore<Notification> briefUserNotifier, PrevalentBubbleBlower prevalentBubbleBlower, Asker asker, TransferQueue transfer) {
		_user = user;
		_channel = channel;
		_contacts = contacts;
		_contactAttributes = contactAttributes;
		_ownName = ownName;
		_briefUserNotifier = briefUserNotifier;
		_prevalentBubbleBlower = prevalentBubbleBlower;
		_asker = asker;
		_transfer = transfer;
	}

}
