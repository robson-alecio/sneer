package wheel.io.files.impl.tranzient;

import java.util.ArrayList;
import java.util.List;

class TransientFile {

	private final List<Byte> _contents;

	TransientFile() {
		_contents = new ArrayList<Byte>();
	}


	List<Byte> contents() {
		return _contents;
	}

}
