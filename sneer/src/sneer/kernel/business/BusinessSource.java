package sneer.kernel.business;

import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.business.contacts.ContactInfo2;
import sneer.kernel.business.contacts.ContactPublicKeyInfo;
import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.lang.Pair;

public interface BusinessSource {

	Business output();

	Omnivore<String> ownNameSetter();
	Omnivore<String> publicKeySetter();
	Omnivore<String> languageSetter();
	
	Consumer<Integer> sneerPortSetter();
	
	Consumer<ContactInfo2> contactAdder2();
	Omnivore<ContactId> contactRemover();
	Consumer<Pair<ContactId, String>> contactNickChanger();
	Omnivore<ContactPublicKeyInfo> contactPublicKeyUpdater();

	@Deprecated
	Consumer<sneer.kernel.business.contacts.ContactInfo> contactAdder();
	@Deprecated
	Omnivore<sneer.kernel.business.contacts.OnlineEvent> contactOnlineSetter();
}