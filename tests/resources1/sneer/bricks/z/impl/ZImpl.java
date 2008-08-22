package sneer.bricks.z.impl;


import org.apache.log4j.Logger;

import sneer.bricks.z.Helper;
import sneer.bricks.z.Z;

class ZImpl implements Z {

	private Logger _log = Logger.getLogger("category");
	
	@Override
	public String doSomething() {
		_log.trace("doSomething() called"); 
		return "SampleImpl";
	}
	
	public ClassLoader libClassLoader() {
		return Logger.class.getClassLoader();
	}

	@Override
	public Helper helper() {
		return new Helper() {};
	}
	
}