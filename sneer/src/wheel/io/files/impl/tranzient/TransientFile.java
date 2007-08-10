package wheel.io.files.impl.tranzient;

import java.util.ArrayList;
import java.util.List;

import wheel.io.files.File;

class TransientFile implements File {

	private final List<Byte> _contents;
	private long _modificationTime;

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

}
