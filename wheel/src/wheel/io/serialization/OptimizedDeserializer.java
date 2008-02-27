package wheel.io.serialization;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;


public class OptimizedDeserializer {

	private final ClassLoader _classloader;
	private final ReusableByteInputStream _inputBytes = new ReusableByteInputStream();
	private ObjectInputStream _inputStream;
	
	public OptimizedDeserializer(ClassLoader classloader) {
		_classloader = classloader;
	}
	
	public Object deserialize(byte[] bytes) throws ClassNotFoundException {
		_inputBytes.refil(bytes);
		try {
			return produceInputStream().readObject();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
	
	private ObjectInputStream produceInputStream() throws IOException {
		if (_inputStream == null)
			_inputStream = new ObjectInputStreamWithClassLoader(_inputBytes, _classloader);
		return _inputStream;
	}

	static public class ReusableByteInputStream extends ByteArrayInputStream {
		public ReusableByteInputStream(){
			super(new byte[0]);
		}
		
		public void refil(byte[] contents){
			pos = 0;
			buf = contents;
			count =contents.length;
		}
	}

}
