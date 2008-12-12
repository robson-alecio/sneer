package snapps.wind.gui.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import snapps.wind.Shout;
import sneer.kernel.container.Inject;
import sneer.pulp.contacts.Contact;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.own.name.OwnNameKeeper;

abstract class ShoutUtils {

	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("HH:mm:ss");
	
	@Inject
	static private OwnNameKeeper _ownName;
	
	@Inject
	static private KeyManager _keyManager;

	static String publisherNick(Shout shout) {
		if(isMyOwnShout(shout)) return _ownName.name().currentValue();
		Contact contact = _keyManager.contactGiven(shout.publisher());
		return contact == null
			? "Unknown Public Key: " + shout.publisher() + " "
			: contact.nickname().currentValue() + " ";
	}

	static String getFormatedShoutTime(Shout shout) {
		return FORMAT.format(new Date(shout.publicationTime()));
	}

	static boolean isMyOwnShout(Shout shout) {
		return _keyManager.ownPublicKey().equals(shout.publisher());
	}
}
