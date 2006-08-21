package sneer.tests;

import java.io.RandomAccessFile;

import sneer.Boot;

class InstrumentedBoot extends Boot {

	byte[] _expectedHash;

	@Override
	protected byte[] receiveByteArray() throws Exception {
		RandomAccessFile file = new RandomAccessFile("bin/sneer/tests/StrapMock.class", "r");
		byte[] bytecode = new byte[(int)file.length()];
		file.readFully(bytecode);
		return bytecode;
	}

	@Override
	protected String strapClassName() {
		return StrapMock.class.getName();
	}

	@Override
	protected void runStrapFromPeer() throws Exception {
		// TODO Auto-generated method stub
		super.runStrapFromPeer();
	}

	@Override
	protected byte[] expectedHash() {
		return _expectedHash;
	}

	

}
