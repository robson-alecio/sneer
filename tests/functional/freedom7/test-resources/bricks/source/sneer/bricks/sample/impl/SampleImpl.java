package sneer.bricks.sample.impl;

import sneer.bricks.sample.Helper;
import sneer.bricks.sample.Sample;

class SampleImpl implements Sample {

	@Override
	public String doSomething() {
		return "SampleImpl";
	}

	@Override
	public Helper helper() {
		return new Helper() {};
	}
	
}