package functional.freedom7;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.io.File;

import org.apache.commons.lang.SystemUtils;
import org.junit.Test;

import sneer.bricks.z.Z;
import sneer.pulp.deployer.BrickBundle;
import functional.SovereignFunctionalTest;
import functional.SovereignParty;

public abstract class Freedom7Test extends SovereignFunctionalTest {

	private static final String Y = "sneer.bricks.y.Y";
	private static final String Z = "sneer.bricks.z.Z";
	
	
	@Test
	public void testPublishSingleBrick() throws Exception {		
		publishY();
		assertBrickInstallation(Y, publisher());
	}

	@Test
	public void testPublishBrickWithDependencies() throws Exception {
		publishY();
		publishXandZ();
		assertBrickInstallation(Z, publisher());
	}
		
	@Test
	public void testMeTooSingleBrick() throws Exception {
		publishY();
		publishXandZ();
		receiver().meToo(publisher(), Z);
		assertBrickInstallation(Z, receiver());
	
		Z z1 = (Z) publisher().produce(Z);
		ClassLoader libClassLoader1 = z1.libClassLoader();
	
		Z z2 = (Z) receiver().produce(Z);
		ClassLoader libClassLoader2 = z2.libClassLoader();
		
		assertNotSame("LogFactory should have been loaded on a different class loader", libClassLoader1, libClassLoader2);
		assertNotSame(z1, z2);
		assertNotSame(classLoaderFor(z1), classLoaderFor(z2));
	}
	

	private ClassLoader classLoaderFor(Object z2) {
		return z2.getClass().getClassLoader();
	}

	private void assertBrickInstallation(String brickName, SovereignParty party) {
		assertNotNull(party.produce(brickName));
	}

	private void publishY() {
		publisher().publishBrick(new File(sourceFolder(),"source2")); //deploy Y first
	}

	private BrickBundle publishXandZ() {
		return publisher().publishBrick(new File(sourceFolder(),"source"));
	}

	private SovereignParty receiver() {
		return _b;
	}

	private SovereignParty publisher() {
		return _a;
	}


	private File sourceFolder() {
		return new File(SystemUtils.getUserDir(), "/tests/functional/freedom7/test-resources/bricks");
	}

}