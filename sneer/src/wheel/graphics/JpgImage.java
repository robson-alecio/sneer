package wheel.graphics;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;

public class JpgImage implements Serializable {

	private byte[] _jpegFileBytes = {};
	
	public JpgImage(){
	}

	public JpgImage(String path) throws IOException {
		InputStream file = new FileInputStream(path);
		read(file);
	}
	
	public JpgImage(InputStream input) throws IOException {
		read(input);
	}
	
	private void read(InputStream input) throws IOException{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();		
		byte[] block = new byte[4096 * 10];
		while (true) {
			int bytesRead = input.read(block);
			if (bytesRead == -1) break;
			bytes.write(block, 0, bytesRead);
		}
		
		_jpegFileBytes = bytes.toByteArray();
	}

	public InputStream jpegFileContents() {
		return new ByteArrayInputStream(_jpegFileBytes);
	}
	
	public byte[] contents(){
		return _jpegFileBytes;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		if (!(other instanceof JpgImage)) return false;
		return Arrays.equals(_jpegFileBytes, ((JpgImage)other)._jpegFileBytes);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(_jpegFileBytes);
	}
	
	private static final long serialVersionUID = 1L;

}
