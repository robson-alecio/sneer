package sneer.internetaddresskeeper.impl;

import sneer.contacts.Contact;
import sneer.internetaddresskeeper.InternetAddress;
import sneer.internetaddresskeeper.InternetAddressKeeper;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListSource;
import wheel.reactive.lists.impl.ListSourceImpl;

//Fix: make persistent
public class InternetAddressKeeperImpl implements InternetAddressKeeper {

	private ListSource<InternetAddress> _addresses = new ListSourceImpl<InternetAddress>();
	
	@Override
	public ListSignal<InternetAddress> addresses() {
		return _addresses.output();
	}

	@Override
	public void add(Contact contact, String host, int port) {
		for (InternetAddress addr : _addresses.output()) {
			if(addr.contact().equals(contact) 
					&& addr.host().equals(host)
					&& addr.port() == port) {
				//same address?
				return;
			}
		}
		InternetAddress addr = new InternetAddressImpl(contact, host, port);
		_addresses.add(addr);
	}
}
