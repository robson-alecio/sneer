package sneer.bricks.mesh.impl;

import sneer.bricks.contacts.Contact;
import sneer.bricks.keymanager.KeyManager;
import sneer.bricks.keymanager.PublicKey;
import sneer.bricks.mesh.Party;
import sneer.lego.Inject;

abstract class AbstractParty implements Party {

	@Inject
	static protected KeyManager _keyManager;

	@Override
	public Party navigateTo(Contact contact) {
		PublicKey pk = producePublicKeyFor(contact);
		AbstractParty result = (AbstractParty)_keyManager.partyGiven(pk, ProxyFactory.INSTANCE);
		result.addIntermediaryIfNecessary(this);
		return result;
	}

	abstract PublicKey producePublicKeyFor(Contact contact);

	abstract void addIntermediaryIfNecessary(AbstractParty abstractParty);
	abstract int distanceInHops();

	abstract void subscribeTo(PublicKey targetPK, String signalPath, PublicKey intermediaryPK);
	abstract void subscribeToContacts(PublicKey targetPK, PublicKey intermediaryPK);

	abstract Object invoke(PublicKey targetPK, BrickInvocation invocation, PublicKey intermediaryPK);

}
