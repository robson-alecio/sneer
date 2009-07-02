package sneer.bricks.software.bricks.statestore.tests;

import sneer.bricks.software.bricks.statestore.BrickStateStore;

public class BrickStateStoreMock implements BrickStateStore {

	@Override
	public Object readObjectFor(Class<?> brick, ClassLoader classloader) {
		return null;
	}

	@Override
	public void writeObjectFor(Class<?> brickIgnored, Object objectIgnored) {}

}
