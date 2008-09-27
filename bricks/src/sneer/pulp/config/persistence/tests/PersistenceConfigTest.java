package sneer.pulp.config.persistence.tests;

import java.io.File;

import org.junit.Test;

import sneer.kernel.container.ContainerUtils;
import sneer.kernel.container.SneerConfig;
import sneer.kernel.container.impl.SneerConfigImpl;
import sneer.pulp.config.persistence.PersistenceConfig;
import wheel.testutil.TestThatMightUseFiles;

public class PersistenceConfigTest extends TestThatMightUseFiles {
	
	private final File _dir = new File(tmpDirectory(), "foo");
	private final SneerConfig _config = new SneerConfigImpl(_dir);
	private final PersistenceConfig _subject = ContainerUtils.newContainer(_config).produce(PersistenceConfig.class);

	@Test
	public void testPersistenceConfig() {
		assertEquals(_dir, _subject.persistenceDirectory());
	}
	
}
