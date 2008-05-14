package sneer.bricks.mesh.impl;

import sneer.bricks.crypto.Sneer1024;
import sneer.bricks.mesh.Party;
import wheel.lang.Functor;

public class ProxyFactory implements Functor<Sneer1024, Party> {

	static final ProxyFactory INSTANCE = new ProxyFactory();

	@Override
	public Party evaluate(Sneer1024 pk) {
		return new Proxy(pk);
	}



}
