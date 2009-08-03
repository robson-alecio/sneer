package sneer.bricks.hardwaresharing.files.tests;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import sneer.bricks.hardware.io.IO;
import sneer.bricks.hardwaresharing.files.client.FileClient;
import sneer.bricks.hardwaresharing.files.publisher.FilePublisher;
import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.bricks.software.code.classutils.ClassUtils;
import sneer.foundation.brickness.testsupport.BrickTest;

public class FileSharingTest extends BrickTest {

	private final FilePublisher _publisher = my(FilePublisher.class);
	private final FileClient _client = my(FileClient.class);

	@Test (timeout = 3000)
	public void publishSmallFile() throws IOException {
		publishAndFetch(anySmallFile());
	}

	@Test (timeout = 3000)
	public void publishFolderWithAFewFiles() throws IOException {
		publishAndFetch(folderWithAFewFiles());
	}

	private void publishAndFetch(File fileOrFolder) throws IOException {
		Sneer1024 hash = _publisher.publish(fileOrFolder);
		assertNotNull(hash);
		
		File destination = newTempFile();
		_client.fetchContentsInto(destination, anyReasonableDate(), hash);
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

	private long anyReasonableDate() {
		return System.currentTimeMillis();
	}
	
}
