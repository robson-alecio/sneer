package sneer.foundation.environments;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



public class CachingEnvironment implements Environment {

	private final Environment _delegate;
	private final Map<Class<?>, Object> _cache = new ConcurrentHashMap<Class<?>, Object>();

	public CachingEnvironment(Environment delegate) {
		_delegate = delegate;
	}

	@Override
	public synchronized <T> T provide(Class<T> intrface) {
		final Object cachedBinding = _cache.get(intrface);
		if (null != cachedBinding)
			return (T) cachedBinding;
		
		final T newBinding = _delegate.provide(intrface);
		if (null != newBinding)
			_cache.put(intrface, newBinding);
		
		return newBinding;
	}

	public void clear() {
		_cache.clear();
	}

}
