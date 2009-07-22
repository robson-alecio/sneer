package sneer.bricks.softwaresharing.tests;

import static sneer.foundation.environments.Environments.my;

import java.io.File;

import org.junit.Ignore;

import sneer.bricks.software.directoryconfig.DirectoryConfig;
import sneer.bricks.softwaresharing.BrickUniverse;
import sneer.foundation.brickness.testsupport.BrickTest;
import sneer.foundation.lang.exceptions.NotImplementedYet;

@Ignore
public class BrickUniverseTest extends BrickTest {

	{
		my(DirectoryConfig.class).ownBinDirectory().set(ownBinDirectory());
		my(DirectoryConfig.class).platformBinDirectory().set(platformBinDirectory());
	}
	
	@SuppressWarnings("unused")
	private BrickUniverse _subject = my(BrickUniverse.class);

	
	
	
	private File ownBinDirectory() {
		throw new NotImplementedYet(); // Implement
	}

	private File platformBinDirectory() {
		throw new NotImplementedYet(); // Implement
	}

	
	
}
