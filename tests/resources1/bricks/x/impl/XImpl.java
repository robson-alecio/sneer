package bricks.x.impl;

import bricks.x.X;
import sneer.bricks.y.Y;
import sneer.bricks.z.Z;
import sneer.kernel.container.Inject;

class XImpl implements X<String> {

	private static final long serialVersionUID = 1L;

	@Inject
	private Z _z;
	
	@Inject
	private Y _y;
	
	@Override
	public void method(String arg) throws Exception {
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public int compareTo(String o) {
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public String callZ() {
		return _z.doSomething();
	}

	@Override
	public String callY() {
		return _y.nothing();
	}
}