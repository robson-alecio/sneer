package sneer.bricks.other.impl;

import sneer.bricks.a.A;
import sneer.bricks.other.Other;
import sneer.bricks.sample.Sample;
import sneer.lego.Inject;

class OtherImpl implements Other<String> {

	private static final long serialVersionUID = 1L;

	@Inject
	private Sample _sample;
	
	@Inject
	private A _a;
	
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
	public void callA() {
		_a.nothing();
	}
}