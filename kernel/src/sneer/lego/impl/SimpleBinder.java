package sneer.lego.impl;

import java.util.ArrayList;
import java.util.List;

import sneer.lego.Binder;

public class SimpleBinder implements Binder {

	private final List<Object> _implementations = new ArrayList<Object>();
	
	public Binder bind(Object implementation) {
		_implementations.add(implementation);
		return this;
	}
	
	@Override
	public Object instanceFor(Class<?> type) {
		Object result = null;
		for (Object candidate : _implementations) {
			if (!instanceOf(candidate, type)) continue;
			
			if (result != null) throwClash(type, result, candidate);
			result = candidate;
		}
		
		return result;
	}
	
	private void throwClash(Class<?> type, Object imp1, Object imp2) {
		throw new IllegalStateException("Binding clash for type " + type + ". It is implemented by " + imp1 + " and " + imp2);
	}

	private boolean instanceOf(Object candidate, Class<?> type) {
		return type.isAssignableFrom(candidate.getClass());
	}

}
