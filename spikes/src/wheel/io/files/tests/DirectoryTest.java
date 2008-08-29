package wheel.io.files.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Test;

import wheel.io.files.Directory;
import wheel.testutil.TestThatUsesFiles;

public abstract class DirectoryTest extends TestThatUsesFiles {
	
	protected Directory _subject;
	
	protected abstract Directory subject() throws IOException;
	protected abstract String absoluteFileName(String name);

	@Before
	public void setUp() throws IOException {
		_subject = subject();
	}

	@Test
	public void testFileCreationAndDeletion() throws IOException{
		assertFalse(_subject.fileExists("test"));
		try {
			_subject.openFile("test");
			fail("Should raise IOException");
		} catch (IOException e) {
			assertMessageContains(e, "File not found: ");
			assertMessageContains(e, "test");
		}		
		
		OutputStream stream = _subject.createFile("test");
		stream.close();
		assertTrue(_subject.fileExists("test"));

		try {
			stream = _subject.createFile("test");
			fail("Should raise IOException");
		} catch (IOException e) {
			assertEquals("File already exists: " + absoluteFileName("test"), e.getMessage());
		}
		
		_subject.deleteFile("test");
		assertFalse(_subject.fileExists("test"));
		
		try{
			_subject.deleteFile("test");
			fail("Should raise IOException");
		} catch (IOException expected){}
		
	}
	
	private void assertMessageContains(Exception e, String part) {
		assertTrue(e.getMessage().indexOf(part) != -1);
	}

	@Test
	public void testDeleteWithOpenStreams() throws IOException {
		OutputStream out = _subject.createFile("log");
		try {
			_subject.deleteFile("log");
			fail("Should not be allowed to delete a file with a stream open on it.");
		} catch (IOException expected) {}
		out.close();
		
		InputStream in = _subject.openFile("log");
		try {
			_subject.deleteFile("log");
			fail("Should not be allowed to delete a file with a stream open on it.");
		} catch (IOException expected) {}
		in.close();
	}

	@Test
	public void testReadWrite() throws IOException, ClassNotFoundException {

		String data = "this is test data";
		Object data2 = new Double(0.0);
		
		ObjectOutputStream objOut = new ObjectOutputStream(_subject.createFile("log"));
		
		objOut.writeObject(data);
		
		InputStream in = _subject.openFile("log");
		ObjectInputStream objIn = new ObjectInputStream(in);
		assertEquals(data, objIn.readObject());
		
		objOut.writeObject(data2);
		objOut.close();
		assertEquals(data2, objIn.readObject());
		assertEquals(-1, in.read());
		
		objIn.close();
	}

	@Test
	public void testRename() throws IOException {
		_subject.createFile("log").close();
		_subject.renameFile("log", "log2");
		assertFalse(_subject.fileExists("log"));
		assertTrue(_subject.fileExists("log2"));

		try{
			_subject.renameFile("trash", "trash1" );
			fail("Should raise IOException");
		} catch (IOException expected) {}
	}

	@Test
	public void testRenameWithOpenStreams() throws IOException{
		
		OutputStream out = _subject.createFile("log");
		try {
			_subject.renameFile("log", "log2");
			fail("Should not be allowed to rename a file with a stream open on it.");
		} catch (IOException expected) {}
		out.close();
		
		InputStream in = _subject.openFile("log");
		try {
			_subject.renameFile("log", "log2");
			fail("Should not be allowed to rename a file with a stream open on it.");
		} catch (IOException expected) {}
		in.close();
	}

}
