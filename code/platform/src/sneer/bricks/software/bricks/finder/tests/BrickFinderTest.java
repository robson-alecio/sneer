package sneer.bricks.software.bricks.finder.tests;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.junit.Test;

import sneer.bricks.software.bricks.finder.BrickFinder;
import sneer.bricks.software.bricks.finder.tests.fixtures.brick1.BrickWithoutNature;
import sneer.bricks.software.bricks.finder.tests.fixtures.brick2.BrickWithNature;
import sneer.bricks.software.bricks.finder.tests.fixtures.nature.SomeNature;
import sneer.bricks.software.code.compilers.java.tests.JarUtils;
import sneer.foundation.brickness.testsupport.BrickTest;

public class BrickFinderTest extends BrickTest {

	private final BrickFinder _subject = my(BrickFinder.class);
	
	@Test
	public void findBricks() throws IOException {
		File testDir = JarUtils.fileFor(getClass()).getParentFile();
		
		Collection<String> bricks = _subject.findBricks(testDir);

		assertEquals(3, bricks.size());
		assertTrue(bricks.contains(BrickWithoutNature.class.getName()));
		assertTrue(bricks.contains(BrickWithNature.class.getName()));
		assertTrue(bricks.contains(SomeNature.class.getName()));
	}
	
}
