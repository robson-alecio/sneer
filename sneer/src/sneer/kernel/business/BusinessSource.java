package sneer.kernel.business;

import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.business.contacts.ContactInfo;
import sneer.kernel.business.contacts.ContactPublicKeyInfo;
import wheel.graphics.JpgImage;
import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.lang.Pair;

public interface BusinessSource {

	Business output();

	Omnivore<String> ownNameSetter();
	Omnivore<String> publicKeySetter();
	Omnivore<String> languageSetter();
	
	Omnivore<String> thoughtOfTheDaySetter();
	Omnivore<JpgImage> pictureSetter();
	Omnivore<String> profileSetter();
	
	Consumer<Integer> sneerPortSetter();
	
	Consumer<ContactInfo> contactAdder();
	Omnivore<ContactId> contactRemover();
	Consumer<Pair<ContactId, String>> contactNickChanger();
	Omnivore<ContactPublicKeyInfo> contactPublicKeyUpdater();

	@Deprecated
	Omnivore<sneer.kernel.business.contacts.OnlineEvent> contactOnlineSetter();
}