package sneer.bricks.hardware.ram.collections;

import java.util.Collection;

import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.Functor;
import sneer.foundation.lang.Predicate;

@Brick
public interface Collections {
	
    <I,O> Collection<O> map(Collection<I> inputCollection, Functor<? super I, ? extends O> functor);
    <T> Collection<T> filter(Collection<T> inputCollection, Predicate<T> predicate);
    
}
