package sneer.kernel.business;

import java.awt.Font;
import java.io.Serializable;

import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.business.contacts.ContactInfo;
import sneer.kernel.business.contacts.ContactPublicKeyInfo;
import wheel.graphics.JpgImage;
import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.lang.Pair;

public interface BusinessSource extends Serializable{

	Business output();

	Omnivore<String> ownNameSetter();
	Omnivore<String> publicKeySetter();
	Omnivore<String> languageSetter();
	Omnivore<Font> fontSetter();
	
	Omnivore<String> thoughtOfTheDaySetter();
	Omnivore<JpgImage> pictureSetter();
	Omnivore<String> profileSetter();
	
	Consumer<Integer> sneerPortSetter();

	Omnivore<String> msnAddressSetter();
	
	Consumer<ContactInfo> contactAdder();
	Omnivore<ContactId> contactRemover();
	Consumer<Pair<ContactId, String>> contactNickChanger();
	Omnivore<Pair<ContactId, String>> contactMsnAddressChanger();
	Omnivore<ContactPublicKeyInfo> contactPublicKeyUpdater();

	@Deprecated
	Omnivore<sneer.kernel.business.contacts.OnlineEvent> contactOnlineSetter();


}