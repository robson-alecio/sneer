package sneer.kernel.business;

import java.awt.Font;
import java.io.Serializable;

import sneer.kernel.business.contacts.ContactManager;
import wheel.graphics.JpgImage;
import wheel.lang.Consumer;
import wheel.lang.Omnivore;

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

	ContactManager contactManager();

	@Deprecated
	Omnivore<sneer.kernel.business.contacts.OnlineEvent> contactOnlineSetter();


}