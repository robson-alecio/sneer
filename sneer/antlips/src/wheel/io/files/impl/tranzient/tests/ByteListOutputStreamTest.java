package wheel.io.files.impl.tranzient.tests;

import java.io.OutputStream;
import java.util.ArrayList;

import wheel.io.files.impl.CloseableWithListener;
import wheel.io.files.impl.tranzient.ByteListOutputStream;

public class ByteListOutputStreamTest extends CloseableStreamTestBase {

	public void testByteListOutputStream() throws Exception{
		ArrayList<Byte> contents = new ArrayList<Byte>();
		OutputStream stream = new ByteListOutputStream(contents);
		
		stream.write(new byte[]{42, 43});
		
		assertEquals((Byte)(byte)42, contents.get(0));
		assertEquals((Byte)(byte)43, contents.get(1));
	}

	@Override
	protected CloseableWithListener createSubject() {
		return new ByteListOutputStream(new ArrayList<Byte>());
	}

}
