package sneer.apps.filesharing.tests;

import java.io.IOException;
import static sneer.tests.SneerTestDashboard.newTestsShouldRun;
import junit.framework.TestCase;
import wheel.io.files.Directory;
import wheel.io.files.impl.tranzient.TransientDirectory;

public abstract class FileSharingTests extends TestCase {

	private Directory _master;
	private Directory _slave;

	@Override
	protected void setUp() {
		_master = new TransientDirectory();
		_slave = new TransientDirectory();
	}

	public void testFileCreation() throws IOException {
		if (!newTestsShouldRun()) return;
		
		_master.createFile("file1.txt", "abc");
		assertFalse(_slave.fileExists("file1.txt"));
		replicate();
		assertEquals("abc", _slave.contentsAsString("file1.txt"));
	}

	public void testFileDeletion() throws IOException {
		if (!newTestsShouldRun()) return;
		
		_master.createFile("file1.txt", "ignored");
		replicate();
		_master.deleteFile("file1.txt");
		replicate();
		assertFalse(_slave.fileExists("file1.txt"));
	}

	public void testFileRename() throws IOException {
		if (!newTestsShouldRun()) return;
		
		_master.createFile("file1.txt", "abc");
		replicate();
		_master.renameFile("file1.txt", "file2.txt");
		replicate();
		assertFalse(_slave.fileExists("file1.txt"));
		assertEquals("abc", _slave.contentsAsString("file2.txt"));
	}

	public void testFileChange() throws IOException {
		if (!newTestsShouldRun()) return;
		
		_master.createFile("file1.txt", "abc");
		replicate();
		replaceContents(_master, "file1.txt", "def");
		assertEquals("abc", _slave.contentsAsString("file1.txt"));
		replicate();
		assertEquals("def", _slave.contentsAsString("file1.txt"));
	}

	private void replicate() {
		replicate(_master, _slave);		
	}

	protected abstract void replicate(Directory master, Directory slave);
	
	private void replaceContents(Directory directory, String fileName, String newContents) throws IOException {
		directory.deleteFile(fileName);
		directory.createFile(fileName, newContents);
	}

}

