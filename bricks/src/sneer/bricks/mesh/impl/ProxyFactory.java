package sneer.bricks.mesh.impl;

import sneer.bricks.keymanager.PublicKey;
import sneer.bricks.mesh.Party;
import wheel.lang.Functor;

public class ProxyFactory implements Functor<PublicKey, Party> {

	static final ProxyFactory INSTANCE = new ProxyFactory();

	@Override
	public Party evaluate(PublicKey pk) {
		return new PeerProxy(pk);
	}



}
