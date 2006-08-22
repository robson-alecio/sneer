package sneer.tests;

import sneer.Boot;

class InstrumentedBoot extends Boot {

	{
		_strapCode = new byte[]{10,20,30};
	}

	@Override
	protected void runStrap() throws Exception {
		super.runStrap();
	}

	@Override
	protected byte[] expectedHash() {
		byte[] wrongHash = new byte[]{42};
		return wrongHash;
	}

}
