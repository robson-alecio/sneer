package wheel.io.files.impl.tranzient.tests;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import wheel.io.files.impl.CloseableWithListener;
import wheel.io.files.impl.tranzient.ByteListInputStream;

public class ByteListInputStreamTests  extends CloseableStreamTest  {

	public void testByteListInputStream() throws IOException{
		ArrayList<Byte> bytesList = new ArrayList<Byte>();
		ByteListInputStream byteListInputStream = new ByteListInputStream(bytesList);
		
		InputStream stream = byteListInputStream;

		bytesList.add((byte)10);
		bytesList.add((byte)-1);
		
		
		assertEquals((byte)10, stream.read());
		int unsigned = -1 & 0xFF;
		assertEquals(unsigned, stream.read());
		
		
		assertEquals(-1, stream.read()); //EOF
		
		bytesList.add((byte)2);
		assertEquals(2, stream.read());
	}

	@Override
	protected CloseableWithListener createSubject() {
		return new ByteListInputStream(new ArrayList<Byte>());
	}
}
