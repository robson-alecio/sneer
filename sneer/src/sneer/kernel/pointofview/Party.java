package sneer.kernel.pointofview;

import wheel.graphics.JpgImage;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;

public interface Party {

	Signal<String> name();
	Signal<String> thoughtOfTheDay();
	Signal<JpgImage> picture();
	Signal<String> profile();
	
	ListSignal<Contact> contacts();
	Contact currentContact(String nick);

	Signal<String> publicKey();
	Signal<Boolean> publicKeyConfirmed();
//	Signal<Float> trustworthiness(); //  ;)

	Signal<String> host();
	Signal<Integer> port();
	Signal<Boolean> isOnline();

}
