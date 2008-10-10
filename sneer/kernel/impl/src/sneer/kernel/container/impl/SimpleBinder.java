package sneer.kernel.container.impl;

import static wheel.lang.Types.instanceOf;

import java.util.ArrayList;
import java.util.List;

class SimpleBinder {

	private final List<Object> _implementations = new ArrayList<Object>();
	
	synchronized void bind(Object implementation) {
		_implementations.add(implementation);
	}
	
	synchronized Object implementationFor(Class<?> type) {
		Object result = null;
		for (Object candidate : _implementations) { //Optimize preserving clash semantics.
			if (!instanceOf(candidate, type)) continue;
			
			if (result != null) throwClash(type, result, candidate);
			result = candidate;
		}
		
		return result;
	}
	
	private void throwClash(Class<?> type, Object imp1, Object imp2) {
		throw new IllegalStateException("Binding clash for type " + type + ". It is implemented by " + imp1 + " and " + imp2);
	}

}
