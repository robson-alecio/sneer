package sneer.apps.filesharing.impl.tests;

import sneer.apps.filesharing.impl.FileSharingImpl;
import sneer.apps.filesharing.tests.FileSharingTests;
import wheel.io.files.Directory;

public class FileSharingImplTests extends FileSharingTests {

	@Override
	protected void replicate(Directory master, Directory slave) {
		new FileSharingImpl(master, slave);		
	}


}
