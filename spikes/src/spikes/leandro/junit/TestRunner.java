package spikes.leandro.junit;

import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;

import sneer.lego.impl.classloader.EclipseClassLoaderFactory;
import sneer.lego.tests.ContainerTest;

public class TestRunner {

	public static void main(String[] args) throws Exception {
		Class<?> testClass = loadTestClass();
		Runner runner = new JUnit4ClassRunner(testClass);
		RunNotifier notifier = new RunNotifier();
		notifier.addFirstListener(new DetailedListener());
		runner.run(notifier);
		System.out.println("** done **");
	}

	private static Class<?> loadTestClass() throws ClassNotFoundException {
		ClassLoader sneerApi = new EclipseClassLoaderFactory().sneerApi();
		return sneerApi.loadClass(ContainerTest.class.getName());
	}

	protected void info() {
		System.out.println(this.getClass().getName() +" : "+this.getClass().getClassLoader());
	}
}

class DetailedListener extends RunListener {

	@Override
	public void testStarted(Description description) throws Exception {
		System.out.println("-> "+description.getDisplayName());
	}

	@Override
	public void testFailure(Failure failure) throws Exception {
		System.out.println("ERROR: "+failure.getMessage());
		failure.getException().printStackTrace();
	}

	@Override
	public void testFinished(Description description) throws Exception {
		//System.out.println("testFinished: "+description.getDisplayName());
	}

	@Override
	public void testIgnored(Description description) throws Exception {
		
	}

	@Override
	public void testRunFinished(Result result) throws Exception {
		System.out.println("testRunFinished: "+result.getFailureCount());
	}

	@Override
	public void testRunStarted(Description description) throws Exception {
		System.out.println("testRunStarted: "+description.getDisplayName());
	}

	
}
