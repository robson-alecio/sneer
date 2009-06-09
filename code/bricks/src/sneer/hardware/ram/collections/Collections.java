package sneer.hardware.ram.collections;

import java.util.Collection;

import sneer.brickness.Brick;
import sneer.commons.lang.Functor;

@Brick
public interface Collections {
	
    <I,O> Collection<O> collect(Collection<I> inputCollection, Functor<? super I, ? extends O> functor);

}
