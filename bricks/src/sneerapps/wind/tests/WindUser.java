package sneerapps.wind.tests;

import sneer.bricks.keymanager.PublicKey;
import sneerapps.wind.Environment;
import wheel.reactive.sets.SetSignal;


public interface WindUser {

	void connectTo(WindUser peer);
	Environment environment();
	PublicKey publicKey();

	void shout(String string);
	SetSignal<String> shoutsHeard();


}