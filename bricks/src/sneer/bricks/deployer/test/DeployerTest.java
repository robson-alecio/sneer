package sneer.bricks.deployer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Test;

import sneer.bricks.config.SneerConfig;
import sneer.bricks.deployer.BrickBundle;
import sneer.bricks.deployer.Deployer;
import sneer.lego.Binder;
import sneer.lego.Inject;
import sneer.lego.impl.SimpleBinder;
import sneer.lego.tests.BrickTestSupport;

public class DeployerTest extends BrickTestSupport {

	@Inject
	private Deployer deployer;
	
	@Override
	protected Binder getBinder() {
		return new SimpleBinder().bind(SneerConfig.class).to(SneerConfigMock.class);
	}

	@Test
	public void testDeploy() throws Exception {
		
		fail("This test is freezing on windows");
		
		//create jar file
		String brickName = "myBrick";
		String version = "1.0";
		File root = getRoot("dev");
		BrickBundle brick = deployer.pack(root);
		
		//test sneer meta
		assertEquals(brickName, brick.getBrickName());
		assertEquals(version, brick.getBrickVersion());
		
		//deploy jar file
		deployer.deploy(brick);
		//TODO: compare files, test compilation, etc
	}
	
	@Test
	public void testDependency() throws Exception {

		fail("This test is freezing on windows");
		
		File root = getRoot("notAlone");
		BrickBundle brick = deployer.pack(root);
		deployer.deploy(brick);
		
	}

	private File getRoot(String name) {
		String dir = System.getProperty("user.dir") + File.separator 
		+ "bricks" + File.separator
		+ "deployer" + File.separator
		+ "test-resources" + File.separator
		+ name + File.separator;
		return new File(dir);
	}

}
