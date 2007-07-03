package sneer.kernel.business;

import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.business.contacts.ContactInfo;
import sneer.kernel.business.contacts.ContactPublicKeyInfo;
import sneer.kernel.business.contacts.OnlineEvent;
import wheel.lang.Consumer;
import wheel.lang.Omnivore;

public interface BusinessSource {

	Business output();

	Omnivore<String> ownNameSetter();
	Omnivore<String> publicKeySetter();
	
	Consumer<Integer> sneerPortSetter();
	
	Consumer<ContactInfo> contactAdder();
	Omnivore<ContactPublicKeyInfo> contactPublicKeyUpdater();
	Omnivore<OnlineEvent> contactOnlineSetter();

	Omnivore<ContactId> contactRemover();

}