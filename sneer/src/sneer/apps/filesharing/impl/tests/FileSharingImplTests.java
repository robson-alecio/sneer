package sneer.apps.filesharing.impl.tests;

import sneer.apps.filesharing.impl.FileSharingImpl;
import sneer.apps.filesharing.tests.FileSharingTests;
import wheel.io.files.Directory;
import wheel.io.files.impl.tranzient.TransientDirectory;

public class FileSharingImplTests extends FileSharingTests {

	private FileSharingImpl _subject;

	@Override
	protected void setUp() {
		TransientDirectory master = new TransientDirectory();
		TransientDirectory slave = new TransientDirectory();
		_subject = new FileSharingImpl(master, slave);
	}

	@Override
	protected Directory master() {
		return _subject.master();
	}

	@Override
	protected Directory slave() {
		return _subject.slave();
	}

	@Override
	protected void replicate() {
		_subject.replicate();
	}

}
