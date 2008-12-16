package tests;


import static wheel.lang.Environments.my;

import org.junit.Before;
import org.junit.runner.RunWith;

import sneer.kernel.container.Injector;
import sneer.pulp.config.persistence.mocks.PersistenceConfigMock;
import wheel.testutil.TestThatMightUseResources;

@RunWith(JMockContainerEnvironment.class)
public abstract class TestThatIsInjected extends TestThatMightUseResources {
	
	@Contribute final PersistenceConfigMock _persistenceConfig = new PersistenceConfigMock(tmpDirectory());
    
	{
		my(ContainerEnvironment.class).instanceBeingInitialized(this);
	}
	
	@Before
	final public void beforeTestThatIsInjected() throws Exception {
		injectDependencies();
	}

	private void injectDependencies() {
	    Injector injector = my(Injector.class);
	    injector.inject(this);
	}

}
