package sneer.pulp.config.persistence.testsupport;


import sneer.brickness.testsupport.BrickTestWithContributions;
import sneer.brickness.testsupport.Contribute;
import sneer.pulp.config.persistence.mocks.PersistenceConfigMock;

public abstract class BrickTest extends BrickTestWithContributions {
	
	@Contribute final PersistenceConfigMock _persistenceConfig = new PersistenceConfigMock(tmpDirectory());
	
}
