package sneer.foundation.environments;

import sneer.foundation.lang.Functor;



public class CachingEnvironment implements Environment {

	private final ResolvingCache<Class<?>, Object> _cache = new ResolvingCache<Class<?>, Object>(new Functor<Class<?>, Object>(){ @Override public Object evaluate(Class<?> need) {
		return _delegate.provide(need);
	}});
	
	private final Environment _delegate;

	public CachingEnvironment(Environment delegate) {
		_delegate = delegate;
	}

	@Override
	public <T> T provide(Class<T> need) {
		return (T)_cache.produce(need);
	}

	public void clear() {
		_cache.clear();
	}

}
