package sneer.pulp.clock.tests;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import sneer.kernel.container.Inject;
import sneer.pulp.clock.Clock;
import sneer.pulp.exceptionhandling.ExceptionHandler;
import sneer.pulp.exceptionhandling.Fallible;
import sneer.pulp.threadpool.Stepper;
import tests.TestThatIsInjected;

@RunWith(JMock.class)
public class ClockShieldingTest extends TestThatIsInjected {

	@Inject static private Clock _subject;
	private final Mockery _mockery = new JUnit4Mockery();
	private final ExceptionHandler _handlerMock = _mockery.mock(ExceptionHandler.class);
	private final Stepper _stepper = new Stepper() { @Override public boolean step() {
		return false;
	}};
	
	{
		_mockery.checking(new Expectations() {{
			one(_handlerMock).shield(with(aNonNull(Fallible.class)));
		}});
	}
	
	@Override
	protected Object[] getBindings() {
		return new Object[] { _handlerMock };
	}
	
	@Test
	public void testAlarmShielding() {
		_subject.wakeUpEvery(10, _stepper);
		_subject.advanceTime(10);
	}
}
