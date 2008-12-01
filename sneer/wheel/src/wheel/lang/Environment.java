package wheel.lang;

public class Environment {
	
	public interface Provider {
		<T> T provide(Class<T> intrface);
	}
	
	public static Provider current() {
		return _provider.get();
	}
	
	public static <T> void runWith(Provider provider, Runnable runnable) {
		final Provider previous = current();
		
//		System.out.println("setting provider" + Thread.currentThread());
		_provider.set(provider);
		try {
			runnable.run();
		} finally {
//			System.out.println("resetting provider" + Thread.currentThread());
			_provider.set(previous);
		}
	}
	
	public static <T> T my(Class<T> brickType) {
		final Provider provider = current();
		if (provider == null)
			throw new IllegalStateException("Unable to provide thread " + Thread.currentThread() + " with implementation for " + brickType);
		return provider.provide(brickType);
	}
	
	private final static ThreadLocal<Provider> _provider = new ThreadLocal<Provider>() {
		@Override
		protected Provider initialValue() {
			return null;
		};
	};
	
}
