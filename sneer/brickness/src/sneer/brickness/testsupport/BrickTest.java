package sneer.brickness.testsupport;


import static sneer.brickness.environments.Environments.my;

import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.internal.ExpectationBuilder;
import org.junit.runner.RunWith;

import sneer.commons.testutil.TestThatMightUseResources;
import sneer.pulp.config.persistence.mocks.PersistenceConfigMock;
import sneer.pulp.tuples.TupleSpace;

@RunWith(BrickTestWithMockRunner.class)
public abstract class BrickTest extends TestThatMightUseResources {
	
	private final Mockery _mockery = new JUnit4Mockery();
	@Contribute final PersistenceConfigMock _persistenceConfig = new PersistenceConfigMock(tmpDirectory());
    
	{
		my(BrickTestRunner.class).instanceBeingInitialized(this);
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

	protected void waitForTupleDispatch() {
		my(TupleSpace.class).waitForAllDispatchingToFinish();
	}
}
