package sneer.kernel.business.contacts;

import wheel.graphics.JpgImage;
import wheel.reactive.Signal;

public interface ContactAttributes {
	
	Signal<String> nick();
	Signal<String> host();
	Signal<Integer> port();

	Signal<String> thoughtOfTheDay();
	Signal<JpgImage> picture();
	Signal<String> profile();
	
	Signal<String> publicKey();
	Signal<Boolean> publicKeyConfirmed();

	ContactId id();
}
