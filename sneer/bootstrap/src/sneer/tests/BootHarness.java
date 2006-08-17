package sneer.tests;

import java.io.RandomAccessFile;

import sneer.Boot;

class BootHarness extends Boot {

	@Override
	protected byte[] receiveByteArray() throws Exception {
		RandomAccessFile file = new RandomAccessFile("bin/sneer/tests/StrapMock.class", "r");
		byte[] bytecode = new byte[(int)file.length()];
		file.readFully(bytecode);
		return bytecode;
	}

	@Override
	protected void run() {
		super.run();
	}

	@Override
	protected String promptForHostnameAndPort() throws Exception {
		return "testing:123";
	}

	

}
