package sneer.bricks.hardware.ram.ref.immutable;

import sneer.foundation.brickness.Brick;

@Brick
public interface Immutables {

	/** @return an immutable that has not been set yet. It must be set before getting its value.*/
	<T> Immutable<T> newInstance();

	/** @return an immutable set to value.*/
	<T> Immutable<T> newInstance(T value);

}
