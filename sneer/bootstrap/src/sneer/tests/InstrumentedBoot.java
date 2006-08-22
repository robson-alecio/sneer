package sneer.tests;

import sneer.Boot;

class InstrumentedBoot extends Boot {

	byte[] _expectedHash;
	
	{
		_strapCode = new byte[]{10,20,30};
	}

	@Override
	protected void runStrap() throws Exception {
		super.runStrap();
	}

	@Override
	protected byte[] expectedHash() {
		return _expectedHash;
	}

}
