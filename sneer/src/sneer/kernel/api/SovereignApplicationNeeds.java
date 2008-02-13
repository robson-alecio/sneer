package sneer.kernel.api;

import sneer.kernel.business.contacts.ContactAttributes;
import sneer.kernel.communication.Channel;
import sneer.kernel.pointofview.Contact;
import sneer.kernel.transferqueue.TransferQueue;
import wheel.io.ui.User;
import wheel.io.ui.User.Notification;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;

public interface SovereignApplicationNeeds {

	User user();
	Omnivore<Notification> briefUserNotifier();

	Channel channel();

	Signal<String> ownName();
	ListSignal<Contact> contacts();
	ListSignal<ContactAttributes> contactAttributes(); //Refactor: Remove this method. It is apparently redundant with contacts().

	TransferQueue transfer(); //Refactor: This will be a service provided by another app, when we have a service framework defined.
	
	Object prevalentState();

}
