package sneer.games.mediawars.mp3sushi;

import java.io.BufferedInputStream;
import java.io.InputStream;

public class BufferedInputStreamTransparent extends BufferedInputStream {

	public BufferedInputStreamTransparent(InputStream _in) {
		super(_in);
		// TODO Auto-generated constructor stub
	}

	public BufferedInputStreamTransparent(InputStream _in, int size) {
		super(_in, size);
		// TODO Auto-generated constructor stub
	}
	
	public int position() {
		return pos;
	}
	
	public int bufferLenght() {
		return buf.length;
	}

}
