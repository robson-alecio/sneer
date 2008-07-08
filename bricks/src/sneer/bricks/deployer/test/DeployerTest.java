package sneer.bricks.deployer.test;

import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Ignore;
import org.junit.Test;

import sneer.bricks.deployer.BrickBundle;
import sneer.bricks.deployer.Deployer;
import sneer.lego.Inject;
import sneer.lego.tests.BrickTestSupport;

@Ignore
public class DeployerTest extends BrickTestSupport {

	@Inject
	private Deployer deployer;
	
	@Override
	protected Object[] getBindings() {
		return new Object[]{new SneerConfigMock(null)};
	}

	@Test
	public void testDeploy() throws Exception {
		fail("This test is freezing on windows");
		
		//create jar file
		File root = getRoot("dev");
		BrickBundle bundle = deployer.pack(root);
		fail("test bundle: "+bundle);
	}
	
	@Test
	public void testDependency() throws Exception {
		fail("This test is freezing on windows");
		
		File root = getRoot("notAlone");
		BrickBundle bundle = deployer.pack(root);
		fail("test bundle: "+bundle);
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
