package sneer.apps.filesharing.tests;

import junit.framework.TestCase;

public abstract class FileSharingTests extends TestCase {

	protected interface Directory {
		void create(String name, String contents);
		void replaceContents(String name, String newContents);
		void rename(String name, String newName);
		void delete(String name);

		boolean hasNamed(String name);
		void checkContents(String name, String contents);
	}

	protected abstract Directory master();
	protected abstract Directory slave();
	protected abstract void replicate();

	public void testFileCreation() {
		master().create("file1.txt", "abc");
		assertFalse(slave().hasNamed("file1.txt"));
		replicate();
		slave().checkContents("file1.txt", "abc");
	}

	public void testFileDeletion() {
		master().create("file1.txt", "ignored");
		replicate();
		master().delete("file1.txt");
		replicate();
		assertFalse(slave().hasNamed("file1.txt"));
	}

	public void testFileRename() {
		master().create("file1.txt", "abc");
		replicate();
		master().rename("file1.txt", "file2.txt");
		replicate();
		assertFalse(slave().hasNamed("file1.txt"));
		slave().checkContents("file2.txt", "abc");
	}

	public void testFileChange() {
		master().create("file1.txt", "abc");
		replicate();
		master().replaceContents("file1.txt", "def");
		slave().checkContents("file1.txt", "abc");
		replicate();
		slave().checkContents("file1.txt", "def");
	}


}
