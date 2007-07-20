package sneer.apps.filesharing.impl;

import wheel.io.files.Directory;
import wheel.io.files.impl.tranzient.TransientDirectory;

public class FileSharingImpl {

	private final TransientDirectory _master;
	private final TransientDirectory _slave;

	public FileSharingImpl(TransientDirectory master, TransientDirectory slave) {
		_master = master;
		_slave = slave;
	}

	public Directory master() {
		return _master;
	}

	public Directory slave() {
		return _slave;
	}

	public void replicate() {
		// Implement Auto-generated method stub
		
	}

}
