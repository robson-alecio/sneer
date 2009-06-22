package sneer.bricks.software.bricks.snappstarter.tests;

import static sneer.foundation.environments.Environments.my;

import org.junit.Ignore;
import org.junit.Test;

import sneer.bricks.software.bricks.snappstarter.SnappStarter;
import sneer.foundation.brickness.SneerHome;
import sneer.foundation.brickness.testsupport.BrickTest;
import sneer.foundation.brickness.testsupport.Bind;

public class SnappStarterTest extends BrickTest {


	@Bind final SneerHome _storagePath = new SneerHome(){@Override public String get() {
		return tmpDirectory().getAbsolutePath();
	}};

	@Ignore
	@Test
	public void findAndLoadSnapps() {
		my(SnappStarter.class);
		
	}
	
}
