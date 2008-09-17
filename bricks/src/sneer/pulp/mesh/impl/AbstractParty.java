package sneer.pulp.mesh.impl;

import sneer.kernel.container.Brick;
import sneer.pulp.contacts.Contact;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.keymanager.PublicKey;
import sneer.pulp.mesh.Party;

abstract class AbstractParty implements Party {

	@Override
	public Party navigateTo(Contact contact) {
		PublicKey pk = producePublicKeyFor(contact);
		AbstractParty result = (AbstractParty)keyManager().partyGiven(pk, ProxyFactory.INSTANCE);
		result.addIntermediaryIfNecessary(this);
		return result;
	}

	abstract PublicKey producePublicKeyFor(Contact contact);

	protected abstract KeyManager keyManager();

	abstract void addIntermediaryIfNecessary(AbstractParty abstractParty);
	abstract int distanceInHops();

	abstract void subscribeTo(PublicKey targetPK, String signalPath, PublicKey intermediaryPK);
	abstract void subscribeToContacts(PublicKey targetPK, PublicKey intermediaryPK);

	abstract void subscribeTo(PublicKey targetPK, Class<? extends Brick> brickInterface, String signalName, PublicKey intermediaryPK);

}
