package functional.freedom7;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import sneer.kernel.container.Brick;
import sneer.kernel.container.Inject;
import sneer.pulp.tmpDirectory.TmpDirectory;
import bricks.z.Z;
import functional.SovereignFunctionalTest;
import functional.SovereignParty;

public abstract class Freedom7Test extends SovereignFunctionalTest {
	
	@Inject
	private TmpDirectory _tmpDirectory;

	@Test
	public void testPublishSingleBrick() throws Exception {		
		publishY();
//		assertBrickInstallation(Y.class, publisher());
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

	private void publishY() throws IOException {
		publisher().publishBricks(generateY());
	}

	private File generateY() throws IOException {
		File root = sourceFolder("src-y");
		
		SourceFileWriter writer = new SourceFileWriter(root);
		writer.write("freedom7.tests.Y", "public interface Y extends sneer.kernel.container.Brick {}");
		writer.write("freedom7.tests.impl.YImpl", "class YImpl implements freedom7.tests.Y {}");
		
		return root;
	}

	private void publishXandZ() throws IOException {
		publisher().publishBricks(generateXZ());
	}

	private File generateXZ() throws IOException {
		return sourceFolder("src-xz");
	}

	private SovereignParty receiver() {
		return _b;
	}

	private SovereignParty publisher() {
		return _a;
	}


	private File sourceFolder(String sourceFolder) throws IOException {
		return _tmpDirectory.createTempDirectory(sourceFolder);
	}

}