package sneer.bricks.hardware.ram.collections;

import java.util.Collection;

import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.Functor;

@Brick
public interface Collections {
	
    <I,O> Collection<O> collect(Collection<I> inputCollection, Functor<? super I, ? extends O> functor);

}
