package sneer.apps.filesharing.tests;

import static sneer.tests.SneerTestDashboard.newTestsShouldRun;

import java.io.IOException;

import sneer.apps.filesharing.FileReplicator;
import wheel.io.files.Directory;
import wheel.io.files.impl.tranzient.TransientDirectory;
import wheel.lang.exceptions.NotImplementedYet;
import wheel.testutil.TestOfInterface;

public abstract class FileReplicatorTests extends TestOfInterface<FileReplicator> {

	private Directory _master;
	private Directory _slave;

	@Override
	protected void setUp() {
		super.setUp();
		_master = new TransientDirectory();
		_slave = new TransientDirectory();
	}

	public void testFileCreation() throws IOException {
		_master.createFile("file1.txt", "abc");
		assertFalse(_slave.fileExists("file1.txt"));
		replicate();
		assertEquals("abc", _slave.contentsAsString("file1.txt"));
	}

	public void testFileDeletion() throws IOException {
		_master.createFile("file1.txt", "ignored");
		replicate();
		_master.deleteFile("file1.txt");
		replicate();
		assertFalse(_slave.fileExists("file1.txt"));
	}

	public void testFileRename() throws IOException {
		_master.createFile("file1.txt", "abc");
		replicate();
		_master.renameFile("file1.txt", "file2.txt");
		replicate();
		assertFalse(_slave.fileExists("file1.txt"));
		assertEquals("abc", _slave.contentsAsString("file2.txt"));
	}

	public void testFileChange() throws IOException {
		_master.createFile("file1.txt", "abc");
		replicate();
		replaceContents(_master, "file1.txt", "def");
		assertEquals("abc", _slave.contentsAsString("file1.txt"));
		replicate();
		assertEquals("def", _slave.contentsAsString("file1.txt"));
	}

	public void testEfficiency() throws IOException {
		if (!newTestsShouldRun()) return;
		
		_master.createFile("file1.txt", "abc");
		replicate();
				
		_master.createFile("file2.txt", "def");
		replicate();
		
		throw new NotImplementedYet();  //Implement Create RemoteDirectory and measure efficiency of transmission on the socket.
	}

	private void replicate() throws IOException {
		_subject.replicate(_master, _slave);		
	}

	private void replaceContents(Directory directory, String fileName, String newContents) throws IOException {
		directory.deleteFile(fileName);
		directory.createFile(fileName, newContents);
	}

}

