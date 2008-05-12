package sneer.bricks.x.impl;

import sneer.bricks.x.X;
import sneer.bricks.y.Y;
import sneer.bricks.z.Z;
import sneer.lego.Inject;

class XImpl implements X<String> {

	private static final long serialVersionUID = 1L;

	@Inject
	private Z _sample;
	
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
	public String callSample() {
		return _sample.doSomething();
	}

	@Override
	public void callY() {
		_y.nothing();
	}
}