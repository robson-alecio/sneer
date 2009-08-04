package sneer.foundation.brickness.testsupport;


import static sneer.foundation.environments.Environments.my;

import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.internal.ExpectationBuilder;
import org.junit.After;
import org.junit.runner.RunWith;

import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.software.folderconfig.FolderConfig;
import sneer.foundation.environments.Environment;
import sneer.foundation.testsupport.CleanTest;

@RunWith(BrickTestWithMockRunner.class)
public abstract class BrickTest extends CleanTest {
	
	private final Mockery _mockery = new JUnit4Mockery();
    
	{
		my(BrickTestRunner.class).instanceBeingInitialized(this);
		my(FolderConfig.class).dataFolder().set(tmpFolder());
	}
	
	
	@After
	public void afterBrickTest() {
		my(Threads.class).crashAllThreads();
		my(BrickTestRunner.class).dispose();
	}
	
	protected Sequence newSequence(String name) {
		return _mockery.sequence(name);
	}
	
	protected <T> T mock(Class<T> type) {
		return _mockery.mock(type);
	}
	
	protected void checking(ExpectationBuilder expectations) {
		_mockery.checking(expectations);
	}

	protected Environment newTestEnvironment(Object... bindings) {
		return my(BrickTestRunner.class).newTestEnvironment(bindings);
	}
}
