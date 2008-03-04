package sneer.deployer.test;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import sneer.config.SneerConfig;
import sneer.deployer.Deployer;
import sneer.lego.Binder;
import sneer.lego.Brick;
import sneer.lego.impl.SimpleBinder;
import sneer.lego.tests.BrickTestSupport;

public class DeployerTest extends BrickTestSupport {

	@Brick
	private Deployer deployer;
	

	
	@Override
	protected Binder getBinder() {
		return new SimpleBinder().bind(SneerConfig.class).to(SneerConfigMock.class);
	}



	@Test
	public void testDeploy() throws Exception {
		Object sample = deployer.deploy("sample");
		assertNotNull(sample);
	}
}
