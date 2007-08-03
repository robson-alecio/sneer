package sneer.kernel.pointofview.tests;

import java.io.IOException;
import static sneer.tests.SneerTestDashboard.newTestsShouldRun;
import junit.framework.TestCase;
import wheel.io.files.Directory;

public abstract class PointOfViewTests extends TestCase {

	protected abstract Directory master();
	protected abstract Directory slave();
	protected abstract void replicate();

	public void testFileCreation() throws IOException {
		if (!newTestsShouldRun()) return;
		
		master().createFile("file1.txt", "abc");
		assertFalse(slave().fileExists("file1.txt"));
		replicate();
		assertEquals("abc", slave().contentsAsString("file1.txt"));
	}

	public void testFileDeletion() throws IOException {
		if (!newTestsShouldRun()) return;
		
		master().createFile("file1.txt", "ignored");
		replicate();
		master().deleteFile("file1.txt");
		replicate();
		assertFalse(slave().fileExists("file1.txt"));
	}

	public void testFileRename() throws IOException {
		if (!newTestsShouldRun()) return;
		
		master().createFile("file1.txt", "abc");
		replicate();
		master().renameFile("file1.txt", "file2.txt");
		replicate();
		assertFalse(slave().fileExists("file1.txt"));
		assertEquals("abc", slave().contentsAsString("file2.txt"));
	}

	public void testFileChange() throws IOException {
		if (!newTestsShouldRun()) return;
		
		master().createFile("file1.txt", "abc");
		replicate();
		replaceContents(master(), "file1.txt", "def");
		assertEquals("abc", slave().contentsAsString("file1.txt"));
		replicate();
		assertEquals("def", slave().contentsAsString("file1.txt"));
	}
	
	private void replaceContents(Directory directory, String fileName, String newContents) throws IOException {
		directory.deleteFile(fileName);
		directory.createFile(fileName, newContents);
	}

}
