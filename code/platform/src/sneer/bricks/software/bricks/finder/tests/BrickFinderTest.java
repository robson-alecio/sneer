package sneer.bricks.software.bricks.finder.tests;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.junit.Test;

import sneer.bricks.software.bricks.finder.BrickFinder;
import sneer.bricks.software.code.compilers.java.tests.JarUtils;
import sneer.bricks.software.directoryconfig.DirectoryConfig;
import sneer.foundation.brickness.testsupport.BrickTest;
import sneer.foundation.testsupport.AssertUtils;

public class BrickFinderTest extends BrickTest {

	private final BrickFinder _subject = my(BrickFinder.class);
	
	@Test
	public void findBricks() throws IOException {
		File testDir = JarUtils.fileFor(getClass()).getParentFile();
		my(DirectoryConfig.class).ownBinDirectory().set(testDir);
		my(DirectoryConfig.class).platformBinDirectory().set(testDir);
		
		Collection<String> bricks = _subject.findBricks();

		AssertUtils.assertSameContents(bricks,
			sneer.bricks.software.bricks.finder.tests.fixtures.brick1.BrickWithoutNature.class.getName(),
			sneer.bricks.software.bricks.finder.tests.fixtures.brick2.BrickWithNature.class.getName(),
			sneer.bricks.software.bricks.finder.tests.fixtures.nature.SomeNature.class.getName()
		);
	}
	
}
