package sneer.foundation.brickness.testsupport.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import sneer.foundation.brickness.testsupport.Intermittent;
import sneer.foundation.brickness.testsupport.IntermittentTestRunner;

@RunWith(IntermittentTestRunner.class)
public class IntermittentTestRunnerTest {

	private static final int MAX_INTERMITTENCES = 3;

	private static boolean _beforeWasCalled = false;
	private static boolean _afterWasCalled = false;
	private static int _intermittenceCounter = 1;

	@Before
	public void beforeIntermittentTest() {
		_beforeWasCalled = true;
	}

	@Ignore
	@Intermittent
	@Test
	public void testIntermittentAnnotation() {
		// DEBUG: System.err.println(_intermittenceCounter);

		checkBeforeAndAfter();

		if (_intermittenceCounter++ == MAX_INTERMITTENCES) return;
		fail();
	}

	@After
	public void afterIntermittentTest() {
		_afterWasCalled = true;
	}

	private void checkBeforeAndAfter() {
		assertTrue(_beforeWasCalled);
		_beforeWasCalled = false;

		if (_intermittenceCounter > 1 && _intermittenceCounter < MAX_INTERMITTENCES) {
			assertTrue(_afterWasCalled);
			_afterWasCalled = false;
		}
	}
}
