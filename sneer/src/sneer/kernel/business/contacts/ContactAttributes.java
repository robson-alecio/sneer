package sneer.kernel.business.contacts;

import wheel.reactive.Signal;

public interface ContactAttributes {
	
	Signal<String> nick();
	Signal<String> host();
	Signal<Integer> port();
	
	Signal<String> publicKey();
	Signal<Boolean> publicKeyConfirmed();
	
	Signal<String> msnAddress();

	ContactId id();
}
