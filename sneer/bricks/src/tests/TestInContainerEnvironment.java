package tests;


import static wheel.lang.Environments.my;

import org.junit.runner.RunWith;

import sneer.pulp.config.persistence.mocks.PersistenceConfigMock;
import wheel.testutil.TestThatMightUseResources;

@RunWith(JMockContainerEnvironment.class)
public abstract class TestInContainerEnvironment extends TestThatMightUseResources {
	
	@Contribute final PersistenceConfigMock _persistenceConfig = new PersistenceConfigMock(tmpDirectory());
    
	{
		my(ContainerEnvironment.class).instanceBeingInitialized(this);
	}
}
