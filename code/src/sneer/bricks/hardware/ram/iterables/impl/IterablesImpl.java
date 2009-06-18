package sneer.bricks.hardware.ram.iterables.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import sneer.bricks.hardware.ram.iterables.Iterables;
import sneer.foundation.lang.Functor;

class IterablesImpl implements Iterables {

	@Override
	public <T> List<T> sortByToString(Iterable<T> iterable) {
		ArrayList<T> result = new ArrayList<T>();
		for (T element : iterable)
			result.add(element);
		
		java.util.Collections.sort(result, TO_STRING_COMPARATOR);
		
		return result;
	}

	@Override
	public <A, B> Iterable<B> map(Iterable<A> source, Functor<A, B> functor) {
		ArrayList<B> result = new ArrayList<B>();
		for (A a : source)
			result.add(functor.evaluate(a));
		return result;
	}

	@Override
	public <E> List<E> toList(Iterable<E> iterable) {
		return toList(iterable.iterator());
	}

	@Override
	public <E> List<E> toList(Iterator<E> iterator) {
		final List<E> result = new ArrayList<E>();
		while (iterator.hasNext())
			result.add(iterator.next());
		return result;
	}

	private Comparator<Object> TO_STRING_COMPARATOR = new Comparator<Object>() {
		@Override
		public int compare(Object o1, Object o2) {
			return String.CASE_INSENSITIVE_ORDER.compare(o1.toString(), o2.toString());
		}
	};	


}
