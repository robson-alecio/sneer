package sneer.pulp.mesh.impl;

import sneer.pulp.keymanager.PublicKey;
import sneer.pulp.mesh.Party;
import wheel.lang.Functor;

class ProxyFactory implements Functor<PublicKey, Party> {

	static final ProxyFactory INSTANCE = new ProxyFactory();

	@Override
	public Party evaluate(PublicKey pk) {
		return new PeerProxy(pk);
	}



}
