package spikes.bamboo;

import sneer.kernel.container.Container;

public class Environment {
	
	private static ThreadLocal<Container> _container = new ThreadLocal<Container>() {
		@Override
		protected Container initialValue() {
			return null;
		};
	};
	
	public static void with(Container container, Runnable runnable) {
		final Container previous = _container.get();
		
		_container.set(container);
		try {
			runnable.run();
		} finally {
			_container.set(previous);
		}
	}
	
	public static <T> T my(Class<T> brickType) {
		final Container container = _container.get();
		if (container == null)
			throw new IllegalStateException();
		return container.produce(brickType);
	}

}
