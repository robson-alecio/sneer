package spikes.wheel.io.ui.graphics;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;

public class JpgImage implements Serializable {

	private final byte[] _jpegFileBytes;
	
	public JpgImage() {
		_jpegFileBytes = new byte[]{};
	}

	public JpgImage(String path) throws IOException {
		InputStream file = new FileInputStream(path);
		_jpegFileBytes = readBytes(file);
	}
	
	public JpgImage(InputStream input) throws IOException {
		_jpegFileBytes = readBytes(input);
	}
	
	private byte[] readBytes(InputStream input) throws IOException{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();		
		byte[] block = new byte[4096 * 10];
		while (true) {
			int bytesRead = input.read(block);
			if (bytesRead == -1) break;
			bytes.write(block, 0, bytesRead);
		}
		
		return bytes.toByteArray();
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
