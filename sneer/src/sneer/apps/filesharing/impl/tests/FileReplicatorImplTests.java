package sneer.apps.filesharing.impl.tests;

import sneer.apps.filesharing.FileReplicator;
import sneer.apps.filesharing.impl.FileReplicatorImpl;
import sneer.apps.filesharing.tests.FileReplicatorTests;

public class FileReplicatorImplTests extends FileReplicatorTests {

	@Override
	protected FileReplicator prepareSubject() {
		return new FileReplicatorImpl();
	}


}
