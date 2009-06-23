package sneer.foundation.brickness.testsupport;


import static sneer.foundation.environments.Environments.my;

import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.internal.ExpectationBuilder;
import org.junit.After;
import org.junit.runner.RunWith;

import sneer.bricks.software.directoryconfig.DirectoryConfig;
import sneer.foundation.testsupport.TestThatMightUseResources;

@RunWith(BrickTestWithMockRunner.class)
public abstract class BrickTest extends TestThatMightUseResources {
	
	private final Mockery _mockery = new JUnit4Mockery();
    
	{
		my(BrickTestRunner.class).instanceBeingInitialized(this);
		my(DirectoryConfig.class).dataDirectory().set(tmpDirectory());
	}
	
	
	@After
	public void afterBrickTest() {
		my(BrickTestRunner.class).dispose();
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
}