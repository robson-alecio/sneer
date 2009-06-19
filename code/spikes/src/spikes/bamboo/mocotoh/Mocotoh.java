package spikes.bamboo.mocotoh;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.internal.runners.TestMethod;

import sneer.foundation.brickness.Brickness;
import sneer.foundation.environments.Environments;

public class Mocotoh extends JUnit4ClassRunner {
	
	public Mocotoh(Class<?> testClass) throws InitializationError {
		super(testClass);
	}
	
	@Override
	protected TestMethod wrapMethod(Method method) {
		return new TestMethod(method, this.getTestClass()) {
			@Override
			public void invoke(final Object test) {
				Environments.runWith(Brickness.newBrickContainer(), new Runnable() { @Override public void run() {
					try {
						superInvoke(test);
					} catch (IllegalArgumentException e) {
						throw new sneer.foundation.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
					} catch (IllegalAccessException e) {
						throw new sneer.foundation.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
					} catch (InvocationTargetException e) {
						throw new sneer.foundation.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
					}
				}});
			}
			
			private void superInvoke(Object test) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
				super.invoke(test);
			}
		};
	}
}
