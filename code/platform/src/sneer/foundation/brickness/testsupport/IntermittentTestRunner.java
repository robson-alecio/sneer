package sneer.foundation.brickness.testsupport;

import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;

public class IntermittentTestRunner extends JUnit4ClassRunner {

	public IntermittentTestRunner(Class<?> testClass) throws InitializationError {
		super(testClass);
	}

}
