package wheel.io.files.impl.tranzient;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import wheel.io.files.File;

class TransientFile implements File {

	private final List<Byte> _contents;
	private long _modificationTime;
	private long _index;

	TransientFile() {
		_contents = new ArrayList<Byte>();
		_modificationTime = System.currentTimeMillis();
	}

	public long modificationTime() {
		return _modificationTime;
	}

	List<Byte> contents() {
		return _contents;
	}

	public OutputStream outputstream() {
		return new MyOutputstream();
	}

	public void seek(long index) {
		_index = index;
	}
	
	private class MyOutputstream extends OutputStream{

		@Override
		public void write(int b) throws IOException {
			try{
				_contents.set((int)_index, new Byte((byte)b));
			}catch(Exception e){
				throw new IOException("Could not write byte "+_index+" : "+b);
			}
		}
	}

}
