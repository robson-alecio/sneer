package sneer.bricks.sample.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sneer.bricks.sample.Helper;
import sneer.bricks.sample.Sample;

class SampleImpl implements Sample {

	private Log _log = LogFactory.getLog("category");
	
	@Override
	public String doSomething() {
		_log.trace("doSomething() called"); 
		return "SampleImpl";
	}
	
	public String logFactory() {
		return LogFactory.getFactory().toString();
	}

	@Override
	public Helper helper() {
		return new Helper() {};
	}
	
}