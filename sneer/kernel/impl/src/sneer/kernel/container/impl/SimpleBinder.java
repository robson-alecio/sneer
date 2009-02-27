package sneer.kernel.container.impl;

import java.util.ArrayList;
import java.util.List;

import sneer.brickness.Environment;


class SimpleBinder implements Environment {

	private final List<Object> _implementations = new ArrayList<Object>();
	
	synchronized void bind(Object implementation) {
		if (implementation == null)
			throw new IllegalArgumentException();
		_implementations.add(implementation);
	}
	
	synchronized Object implementationFor(Class<?> type) {
		Object result = null;
		for (Object candidate : _implementations) { //Optimize preserving clash semantics.
			if (!type.isInstance(candidate)) continue;
			
			if (result != null) throwClash(type, result, candidate);
			result = candidate;
		}
		
		return result;
	}
	
	private void throwClash(Class<?> type, Object imp1, Object imp2) {
		throw new IllegalStateException("Binding clash for type " + type + ". It is implemented by " + imp1 + " and " + imp2);
	}

	@Override
	public <T> T provide(Class<T> intrface) {
		return (T) implementationFor(intrface);
	}

}
