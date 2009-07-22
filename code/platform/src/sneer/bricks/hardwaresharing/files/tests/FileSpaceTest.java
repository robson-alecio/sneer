package sneer.bricks.hardwaresharing.files.tests;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;

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
	public void publishSingleSmallFile() throws IOException {
		File anySmallFile = my(ClassUtils.class).toFile(getClass());
		Sneer1024 hash = _subject.publishContents(anySmallFile);
		
		my(TupleSpace.class).waitForAllDispatchingToFinish();
		
		File destination = new File(tmpDirectory(), "destination.tmp");
		_subject.fetchContentsInto(destination, hash);
		
		assertTrue(my(IO.class).files().contentEquals(anySmallFile, destination));
	}
	
}
