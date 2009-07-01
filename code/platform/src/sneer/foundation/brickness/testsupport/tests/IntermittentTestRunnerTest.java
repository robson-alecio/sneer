package sneer.foundation.brickness.testsupport.tests;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import sneer.foundation.brickness.testsupport.Intermittent;
import sneer.foundation.brickness.testsupport.IntermittentTestRunner;

@RunWith(IntermittentTestRunner.class)
public class IntermittentTestRunnerTest {

	private static boolean _beforeWasCalled = false;
	private static boolean _afterWasCalled = false;

	@Before
	public void beforeIntermittentTest() {
		_beforeWasCalled = true;
	}

	@After
	public void afterIntermittentTest() {
		_afterWasCalled = true;
	}
	
	
	private static int _intermittenceCounter = 1;
	@Ignore
	@Intermittent
	@Test
	public void testIntermittentAnnotation() {
		checkBeforeAndAfter();
		
		if (_intermittenceCounter++ == 3) return;
		fail();
	}

	private void checkBeforeAndAfter() {
		assertTrue(_beforeWasCalled);
		_beforeWasCalled = false;

		if (_intermittenceCounter > 1) {
			assertTrue(_afterWasCalled);
			_afterWasCalled = false;
		}
	}

}
