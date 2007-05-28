package sneer.kernel.business;

import java.io.Serializable;
import java.util.List;

import sneer.kernel.business.contacts.ContactInfo;

import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;

public interface BusinessSource {

	Business output();

	Consumer<String> ownNameSetter();
	
	Consumer<Integer> sneerPortSetter();
	
	Consumer<ContactInfo> contactAdder();

}