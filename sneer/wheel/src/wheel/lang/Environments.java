package wheel.lang;

public class Environments {
	
	public interface Memento {}

	private static Environment current() {
		return _environment.get();
	}
	
	public static <T> void runWith(Environment environment, Runnable runnable) {
		final Environment previous = current();
		
//		System.out.println("setting provider" + Thread.currentThread());
		_environment.set(environment);
		try {
			runnable.run();
		} finally {
//			System.out.println("resetting provider" + Thread.currentThread());
			_environment.set(previous);
		}
	}

	public static <T> void runWith(Memento memento, Runnable runnable) {
		runWith(((MementoImpl)memento)._environment, runnable);
	}

	public static Memento memento() {
		return new MementoImpl(current());
	}
	
	public static <T> T my(Class<T> intrface) {
		final Environment environment = current();
		if (environment == null)
			throw new IllegalStateException("Unable to provide thread " + Thread.currentThread() + " with implementation for " + intrface);
		return environment.provide(intrface);
	}
	
	private final static ThreadLocal<Environment> _environment = new ThreadLocal<Environment>() {
		@Override
		protected Environment initialValue() {
			return null;
		};
	};

}

class MementoImpl implements Environments.Memento {
	MementoImpl(Environment environment) {_environment = environment;}
	final Environment _environment;
}
