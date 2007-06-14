package sneer.kernel.business;

import sneer.kernel.business.contacts.ContactInfo;
import sneer.kernel.business.contacts.OnlineEvent;
import wheel.lang.Consumer;
import wheel.lang.Omnivore;

public interface BusinessSource {

	Business output();

	Omnivore<String> ownNameSetter();
	
	Consumer<Integer> sneerPortSetter();
	
	Consumer<ContactInfo> contactAdder();
	Omnivore<OnlineEvent> contactOnlineSetter();

}