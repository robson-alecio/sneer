package sneer.pulp.contacts.list;

import sneer.kernel.container.Inject;
import sneer.pulp.connection.ConnectionManager;
import sneer.pulp.contacts.Contact;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public class ContactInfo{
	
	protected final Contact _contact;
	protected Signal<Boolean> _isOnline;

	@SuppressWarnings("unused")
	private final Omnivore<Boolean> _onlineReceiverToAvoidGc;

	@Inject
	static private ConnectionManager _connectionManager;
	
	public ContactInfo(Contact contact, Omnivore<Boolean> onlineReceiver) {
		this(contact, onlineReceiver, _connectionManager.connectionFor(contact).isOnline());
	}

	protected ContactInfo(Contact contact, Omnivore<Boolean> onlineReceiver, Signal<Boolean> isOnline) {
		_contact = contact;
		_onlineReceiverToAvoidGc = onlineReceiver;
		_isOnline = isOnline;
		_isOnline.addReceiver(onlineReceiver);
	}

	public Contact contact() {
		return _contact;
	}

	public Signal<Boolean> isOnline() {
		return _isOnline;
	}
}