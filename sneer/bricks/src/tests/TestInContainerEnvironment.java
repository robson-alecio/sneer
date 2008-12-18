package tests;


import static wheel.lang.Environments.my;

import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.internal.ExpectationBuilder;
import org.junit.runner.RunWith;

import sneer.pulp.config.persistence.mocks.PersistenceConfigMock;
import sneer.pulp.threadpool.ThreadPool;
import wheel.testutil.TestThatMightUseResources;

@RunWith(JMockContainerEnvironment.class)
public abstract class TestInContainerEnvironment extends TestThatMightUseResources {
	
	private final Mockery _mockery = new JUnit4Mockery();
	@Contribute final PersistenceConfigMock _persistenceConfig = new PersistenceConfigMock(tmpDirectory());
    
	{
		my(ContainerEnvironment.class).instanceBeingInitialized(this);
	}
	
	protected Sequence sequence(String name) {
		return _mockery.sequence(name);
	}
	
	protected <T> T mock(Class<T> type) {
		return _mockery.mock(type);
	}
	
	protected void checking(ExpectationBuilder expectations) {
		_mockery.checking(expectations);
	}

	protected void waitForDispatch() {
		my(ThreadPool.class).waitForAllDispatchingToFinish();
	}
}
