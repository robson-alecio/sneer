package sneer.bricks.softwaresharing.tests;

import static sneer.foundation.environments.Environments.my;

import java.io.File;

import org.junit.Ignore;

import sneer.bricks.software.folderconfig.FolderConfig;
import sneer.bricks.softwaresharing.BrickSpace;
import sneer.foundation.brickness.testsupport.BrickTest;
import sneer.foundation.lang.exceptions.NotImplementedYet;

@Ignore
public class BrickUniverseTest extends BrickTest {

	{
		my(FolderConfig.class).ownBinFolder().set(ownBinFolder());
		my(FolderConfig.class).platformBinFolder().set(platformBinFolder());
	}
	
	@SuppressWarnings("unused")
	private BrickSpace _subject = my(BrickSpace.class);

	private File ownBinFolder() { throw new NotImplementedYet(); /* Implement*/ }
	private File platformBinFolder() {throw new NotImplementedYet(); /* Implement*/ }
	
}
