package sneer.pulp.datastructures.cache.impl;

import java.util.LinkedList;
import java.util.List;

import sneer.pulp.datastructures.cache.Cache;

class CacheImpl implements Cache {

	private final List<Object> _elements;
	private final int _capacity;
	
	public CacheImpl(int capacity) {
		_capacity = capacity;
		_elements = new LinkedList<Object>();
	}

	@Override
	public void keep(Object object) {
		_elements.add(0, object);
		
		if (_elements.size() > _capacity)
			_elements.remove(_capacity);
	}

	@Override
	public boolean contains(Object obj) {
		return _elements.contains(obj);
	}

	@Override
	public int handleFor(Object object) {
		return _elements.indexOf(object); 
	}

	@Override
	public Object getByHandle(int handle) {
		if (handle < 0) return null;
		if (handle >= _elements.size()) return null;
		
		return _elements.get(handle);
	}

}