package sneer.bricks.mesh.impl;

import sneer.bricks.contacts.Contact;
import sneer.bricks.crypto.Sneer1024;
import sneer.bricks.keymanager.KeyManager;
import sneer.bricks.mesh.Party;
import sneer.lego.Inject;

abstract class AbstractParty implements Party {

	AbstractParty() {
		injectIfNecessary();
	}

	@Inject
	protected KeyManager _keyManager;

	@Override
	public Party navigateTo(Contact contact) {
		Sneer1024 pk = producePublicKeyFor(contact);
		AbstractParty result = (AbstractParty)_keyManager.partyGiven(pk, ProxyFactory.INSTANCE);
		result.addIntermediaryIfNecessary(this);
		return result;
	}

	abstract Sneer1024 producePublicKeyFor(Contact contact);

	abstract void addIntermediaryIfNecessary(AbstractParty abstractParty);
	abstract int distanceInHops();

	abstract void subscribeTo(Sneer1024 targetPK, String signalPath, Sneer1024 intermediaryPK);
	abstract void subscribeToContacts(Sneer1024 targetPK, Sneer1024 intermediaryPK);

	abstract void injectIfNecessary();
}
