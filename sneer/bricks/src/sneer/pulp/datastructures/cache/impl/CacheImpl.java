package sneer.pulp.datastructures.cache.impl;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import sneer.pulp.datastructures.cache.Cache;

class CacheImpl implements Cache {

	private final List<Object> _elementsInOrder;
	private final Set<Object> _elements = new HashSet<Object>();
	private final int _capacity;
	
	public CacheImpl(int capacity) {
		_capacity = capacity;
		_elementsInOrder = new LinkedList<Object>();
	}

	@Override
	public void keep(Object object) {
		removeIfNecessary(object);
		_elementsInOrder.add(0, object);
		_elements.add(object);
		
		if (_elementsInOrder.size() <= _capacity) return;
		
		Object last=_elementsInOrder.remove(_capacity);
		_elements.remove(last);
	}

	private void removeIfNecessary(Object object) {
		_elementsInOrder.remove(object);
	}

	@Override
	public boolean contains(Object obj) {
		return _elements.contains(obj);
	}

	@Override
	public int handleFor(Object object) {
		return _elementsInOrder.indexOf(object); 
	}

	@Override
	public Object getByHandle(int handle) {
		if (handle < 0) return null;
		if (handle >= _elementsInOrder.size()) return null;
		
		return _elementsInOrder.get(handle);
	}

}