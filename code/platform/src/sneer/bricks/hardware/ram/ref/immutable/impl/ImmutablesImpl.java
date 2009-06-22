package sneer.bricks.hardware.ram.ref.immutable.impl;

import sneer.bricks.hardware.ram.ref.immutable.Immutable;
import sneer.bricks.hardware.ram.ref.immutable.Immutables;

class ImmutablesImpl implements Immutables {

	@Override
	public <T> Immutable<T> newInstance() {
		return new ImmutableImpl<T>();
	}

	@Override
	public <T> Immutable<T> newInstance(T value) {
		return new ImmutableImpl<T>(value);
	}

}
