package functional.freedom7;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.io.File;

import org.apache.commons.lang.SystemUtils;
import org.junit.Test;

import sneer.kernel.container.Brick;
import bricks.y.Y;
import bricks.z.Z;
import functional.SovereignFunctionalTest;
import functional.SovereignParty;

public abstract class Freedom7Test extends SovereignFunctionalTest {

	@Test
	public void testPublishSingleBrick() throws Exception {		
		publishY();
		assertBrickInstallation(Y.class, publisher());
	}

	@Test
	public void testPublishBrickWithDependencies() throws Exception {
		publishY();
		publishXandZ();
		assertBrickInstallation(Z.class, publisher());
	}
		
	@Test
	public void testMeTooSingleBrick() throws Exception {
		publishY();
		publishXandZ();
		receiver().meToo(publisher(), Z.class.getName());
		assertBrickInstallation(Z.class, receiver());
	
		Z z1 = (Z) publisher().produce(Z.class);
		ClassLoader libClassLoader1 = z1.libClassLoader();
	
		Z z2 = (Z) receiver().produce(Z.class);
		ClassLoader libClassLoader2 = z2.libClassLoader();
		
		assertNotSame("LogFactory should have been loaded on a different class loader", libClassLoader1, libClassLoader2);
		assertNotSame(z1, z2);
		assertNotSame(classLoaderFor(z1), classLoaderFor(z2));
	}
	

	private ClassLoader classLoaderFor(Object z2) {
		return z2.getClass().getClassLoader();
	}

	private void assertBrickInstallation(Class<? extends Brick> brick, SovereignParty party) {
		assertNotNull(party.produce(brick));
	}

	private void publishY() {
		publisher().publishBricks(sourceFolder("resources2"));
	}

	private void publishXandZ() {
		publisher().publishBricks(sourceFolder("resources1"));
	}

	private SovereignParty receiver() {
		return _b;
	}

	private SovereignParty publisher() {
		return _a;
	}


	private File sourceFolder(String sourceFolder) {
		return new File(SystemUtils.getUserDir(), "/tests/"+sourceFolder);
	}

}