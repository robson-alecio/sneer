/**
 * 
 */
package sneer.bricks.hardware.ram.arrays.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import sneer.bricks.hardware.ram.arrays.ImmutableArray;

final class ImmutableArrayImpl<T> implements ImmutableArray<T> {
	
	private final Object[] _elements;

	ImmutableArrayImpl(Collection<T> elements) {
		_elements = elements.toArray(new Object[elements.size()]);
	}

	@Override
	public Iterator<T> iterator() {
		return (Iterator<T>) Collections.unmodifiableCollection(Arrays.asList(_elements)).iterator();
	}
}