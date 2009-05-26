package sneer.hardware.ram.collections;

import java.util.Iterator;
import java.util.List;

import sneer.brickness.Brick;
import sneer.commons.lang.Functor;

@Brick
public interface Collections {

	<T> List<T> sortByToString(Iterable<T> iterable);

	<A, B> Iterable<B> map(Iterable<A> source, Functor<A, B> functor);

	<E> List<E> toList(Iterable<E> iterable);

	<E> List<E> toList(Iterator<E> iterator);

}