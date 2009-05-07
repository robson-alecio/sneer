package snapps.contacts.gui.info.impl;

import static sneer.commons.environments.Environments.my;

import java.util.Comparator;

import snapps.contacts.gui.ContactsGui;
import sneer.commons.lang.Functor;
import sneer.pulp.contacts.Contact;
import sneer.pulp.internetaddresskeeper.InternetAddress;
import sneer.pulp.internetaddresskeeper.InternetAddressKeeper;
import sneer.pulp.reactive.ReactivePredicate;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.Signals;
import sneer.pulp.reactive.collections.ListSignal;
import sneer.pulp.reactive.collections.SetSignal;
import sneer.pulp.reactive.collections.listsorter.ListSorter;
import sneer.pulp.reactive.collections.setfilter.SetFilter;

class ContactInternetAddressList {

	private final SetSignal<InternetAddress> _filteredAddresses;
	private final ListSignal<InternetAddress> _sortedAddresses;

	ContactInternetAddressList(){
		_filteredAddresses = my(SetFilter.class).filter(my(InternetAddressKeeper.class). addresses(),	reactivePredicate());		
		_sortedAddresses = my(ListSorter.class).sort( _filteredAddresses,  sortByHostName());		
	}
	
	private ReactivePredicate<InternetAddress> reactivePredicate() {
		return new ReactivePredicate<InternetAddress>(){ @Override public Signal<Boolean> evaluate(final InternetAddress address) {
			return my(Signals.class).adapt(
					my(ContactsGui.class).selectedContact(), 
					contactToBolean(address));
		}};
	}

	private Functor<Contact, Boolean> contactToBolean(final InternetAddress address) {
		return new Functor<Contact, Boolean>(){ @Override public Boolean evaluate(Contact contact) {
			return address.contact() == contact;
		}};
	}

	private Comparator<InternetAddress> sortByHostName() {
		return new Comparator<InternetAddress>(){ @Override public int compare(InternetAddress o1, InternetAddress o2) {
			return o1.host().compareTo(o2.host());
		}};
	}
	
	ListSignal<InternetAddress> addresses(){
		return _sortedAddresses;
	}
}