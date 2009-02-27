package snapps.wind.gui.impl;

import static sneer.brickness.Environments.my;

import java.text.SimpleDateFormat;
import java.util.Date;

import snapps.wind.Shout;
import sneer.pulp.contacts.Contact;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.own.name.OwnNameKeeper;

abstract class ShoutUtils {

	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("HH:mm:ss");
	
	private static OwnNameKeeper ownName() {
		return my(OwnNameKeeper.class);
	}
	
	private static KeyManager keyManager() {
		return my(KeyManager.class);
	}

	static String publisherNick(Shout shout) {
		if(isMyOwnShout(shout)) return ownName().name().currentValue();
		Contact contact = keyManager().contactGiven(shout.publisher());
		return contact == null
			? "Unknown Public Key: " + shout.publisher() + " "
			: contact.nickname().currentValue() + " ";
	}

	static String getFormatedShoutTime(Shout shout) {
		return FORMAT.format(new Date(shout.publicationTime()));
	}

	static boolean isMyOwnShout(Shout shout) {
		return keyManager().ownPublicKey().equals(shout.publisher());
	}
}
