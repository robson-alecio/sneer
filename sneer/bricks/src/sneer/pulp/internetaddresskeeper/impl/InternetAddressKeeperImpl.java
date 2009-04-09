package sneer.pulp.internetaddresskeeper.impl;

import sneer.pulp.contacts.Contact;
import sneer.pulp.internetaddresskeeper.InternetAddress;
import sneer.pulp.internetaddresskeeper.InternetAddressKeeper;
import wheel.reactive.lists.ListRegister;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.ListRegisterImpl;

class InternetAddressKeeperImpl implements InternetAddressKeeper {

	private ListRegister<InternetAddress> _addresses = new ListRegisterImpl<InternetAddress>();
	
	@Override
	public void remove(InternetAddress address) {
		_addresses.remove(address);
	}	
	
	@Override
	public ListSignal<InternetAddress> addresses() {
		return _addresses.output();
	}

	@Override
	public void add(Contact contact, String host, int port) { //Implement Handle contact removal.
		if (!isNewAddress(contact, host, port)) return;
		
		InternetAddress addr = new InternetAddressImpl(contact, host, port);
		_addresses.add(addr);
	}

	private boolean isNewAddress(Contact contact, String host, int port) {
		for (InternetAddress addr : _addresses.output())
			if(addr.contact().equals(contact) 
					&& addr.host().equals(host)
					&& addr.port() == port)
				return false;
		
		return true;
	}
}
