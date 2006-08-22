package sneer.tests;

import java.io.RandomAccessFile;

import sneer.Boot;

class InstrumentedBoot extends Boot {

	byte[] _expectedHash;

	@Override
	protected byte[] receiveByteArray() throws Exception {
		String mockStrap = getResource("sneer/tests/StrapMock.class").toURI().getPath();
		RandomAccessFile file = new RandomAccessFile(mockStrap, "r");
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
		super.runStrapFromPeer();
	}

	@Override
	protected byte[] expectedHash() {
		return _expectedHash;
	}

}
