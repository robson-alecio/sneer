package sneer.bricks.pulp.datastructures.cache.impl;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import sneer.bricks.pulp.datastructures.cache.Cache;

class CacheImpl<T> implements Cache<T> {

	private final List<T> _elementsInOrder;
	private final Set<T> _elements = new HashSet<T>();
	private final int _capacity;
	
	public CacheImpl(int capacity) {
		_capacity = capacity;
		_elementsInOrder = new LinkedList<T>();
	}

	@Override
	public void keep(T object) {
		_elementsInOrder.remove(object);
		_elementsInOrder.add(0, object);
		_elements.add(object);
		
		if (_elementsInOrder.size() <= _capacity) return;
		
		Object last=_elementsInOrder.remove(_capacity);
		_elements.remove(last);
	}

	@Override
	public boolean contains(T obj) {
		return _elements.contains(obj);
	}

	@Override
	public int handleFor(T object) {
		return _elementsInOrder.indexOf(object); 
	}

	@Override
	public T getByHandle(int handle) {
		if (handle < 0) return null;
		if (handle >= _elementsInOrder.size()) return null;
		
		return _elementsInOrder.get(handle);
	}

}