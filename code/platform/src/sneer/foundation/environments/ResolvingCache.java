package sneer.foundation.environments;

import java.util.HashMap;
import java.util.Map;

import sneer.foundation.lang.Functor;

public class ResolvingCache<K,V> {

	
	private static final class Placeholder {}


	private final Object _cacheMonitor = new Object();
	private Map<K,Object> _cache = new HashMap<K,Object>();
	
	private final Functor<K, V> _resolver;

	
	public ResolvingCache(Functor<K, V> resolver) {
		_resolver = resolver;
	}

	
	V produce(K key) {
		Object found;

		synchronized (_cacheMonitor) {
			found = _cache.get(key);
			if (found == null)
				_cache.put(key, new Placeholder());
		}

		if (found == null) {
			V resolved = _resolver.evaluate(key);
			Object placeholder = _cache.put(key, resolved);
			synchronized (placeholder) { placeholder.notifyAll(); }
			return resolved;
		}

		if (found instanceof Placeholder) {
			wait(found);
			return (V)_cache.get(key);
		}
		
		return (V)found;
	}

	
	public void clear() {
		_cache.clear();
	}

	
	static private void wait(Object object) {
		try {
			synchronized (object) { object.wait(); }
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}
	
}
