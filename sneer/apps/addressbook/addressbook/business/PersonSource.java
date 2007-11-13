package addressbook.business;

import wheel.lang.Omnivore;

public interface PersonSource {

	Person output();
	
	Omnivore<String> nameSetter();
	Omnivore<String> addressSetter();
	Omnivore<String> emailSetter();
	Omnivore<String> phoneSetter();
	
}
