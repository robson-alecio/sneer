package sneer.apps.filesharing.impl;

import wheel.io.files.Directory;

public class FileSharingImpl {

	private final Directory _master;
	private final Directory _slave;

	public FileSharingImpl(Directory master, Directory slave) {
		_master = master;
		_slave = slave;
		replicate();
	}

	public void replicate() {
		_master.toString();
		_slave.toString();
		// Implement Auto-generated method stub
	}

}
