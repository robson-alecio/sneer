package sneer.bricks.z.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sneer.bricks.z.Helper;
import sneer.bricks.z.Z;

class ZImpl implements Z {

	private Log _log = LogFactory.getLog("category");
	
	@Override
	public String doSomething() {
		_log.trace("doSomething() called"); 
		return "SampleImpl";
	}
	
	public ClassLoader libClassLoader() {
		return LogFactory.class.getClassLoader();
	}

	@Override
	public Helper helper() {
		return new Helper() {};
	}
	
}