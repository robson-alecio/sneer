package sneer.kernel.business.contacts;

import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.lang.Pair;

public interface ContactManager {

	Consumer<ContactInfo> contactAdder();
	Omnivore<ContactId> contactRemover();
	Consumer<Pair<ContactId, String>> contactNickChanger();
	Omnivore<Pair<ContactId, String>> contactMsnAddressChanger();
	Omnivore<ContactPublicKeyInfo> contactPublicKeyUpdater();

}
