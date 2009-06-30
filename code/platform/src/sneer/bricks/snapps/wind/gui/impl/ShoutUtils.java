package sneer.bricks.snapps.wind.gui.impl;

import static sneer.foundation.environments.Environments.my;

import java.text.SimpleDateFormat;
import java.util.Date;

import sneer.bricks.network.social.Contact;
import sneer.bricks.pulp.keymanager.Seals;
import sneer.bricks.pulp.own.name.OwnNameKeeper;
import sneer.bricks.skin.main.synth.Synth;
import sneer.bricks.snapps.wind.Shout;

abstract class ShoutUtils {

	private static final SimpleDateFormat FORMAT = new SimpleDateFormat((String) my(Synth.class).getDefaultProperty("ShoutUtils.dateFormat"));
	
	private static OwnNameKeeper ownName() {
		return my(OwnNameKeeper.class);
	}
	
	private static Seals keyManager() {
		return my(Seals.class);
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
		return keyManager().ownSeal().equals(shout.publisher());
	}
}
