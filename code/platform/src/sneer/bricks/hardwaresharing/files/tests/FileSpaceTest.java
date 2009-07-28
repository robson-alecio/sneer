package sneer.bricks.hardwaresharing.files.tests;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import sneer.bricks.hardware.io.IO;
import sneer.bricks.hardwaresharing.files.FileSpace;
import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.bricks.pulp.tuples.TupleSpace;
import sneer.bricks.software.code.classutils.ClassUtils;
import sneer.foundation.brickness.testsupport.BrickTest;

public class FileSpaceTest extends BrickTest {

	private final FileSpace _subject = my(FileSpace.class);

	@Test (timeout = 3000)
	public void publishSmallFile() throws IOException {
		publishAndFetch(anySmallFile());
	}

	@Ignore
	@Test (timeout = 3000)
	public void publishFolderWithAFewFiles() throws IOException {
		publishAndFetch(folderWithAFewFiles());
	}

	private void publishAndFetch(File fileOrFolder) throws IOException {
		Sneer1024 hash = _subject.publishContents(fileOrFolder);
		assertNotNull(hash);
		
		my(TupleSpace.class).waitForAllDispatchingToFinish();
		
		File destination = newTempFile();
		_subject.fetchContentsInto(destination, hash);
		assertSameContents(fileOrFolder, destination);
	}
	
	private void assertSameContents(File file1, File file2) throws IOException {
		assertTrue(my(IO.class).files().contentEquals(file1, file2));
	}

	private File newTempFile() {
		return new File(tmpFolder(), "destination" + System.nanoTime());
	}

	private File anySmallFile() {
		return myClassFile();
	}

	private File myClassFile() {
		return my(ClassUtils.class).toFile(getClass());
	}

	private File folderWithAFewFiles() {
		return new File(myClassFile().getParent(), "fixtures");
	}
	
}
