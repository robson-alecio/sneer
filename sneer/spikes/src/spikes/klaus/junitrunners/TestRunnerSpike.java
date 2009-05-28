package spikes.klaus.junitrunners;

import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;

public class TestRunnerSpike extends JUnit4ClassRunner {

	
	public TestRunnerSpike(@SuppressWarnings("unused") Class<?> testClass) throws InitializationError {
		super(TestThatPasses.class);
	}
	
	
//	@Override
//	protected void invokeTestMethod(final Method arg0, final RunNotifier arg1) {
//		Environments.runWith(newEnvironment(), new Runnable() { @Override public void run() {
//			superInvokeTestMethod(arg0, arg1);
//		}});
//	}
//
//	@Override
//	protected TestMethod wrapMethod(Method method) {
//		return new TestMethodWithEnvironment(method, getTestClass());
//	}
//
//	private void superInvokeTestMethod(Method arg0, RunNotifier arg1) {
//		super.invokeTestMethod(arg0, arg1);
//	}
	
}
