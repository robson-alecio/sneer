package sneer.bricks.software.bricks.statestore;

import sneer.foundation.brickness.Brick;

@Brick
public interface BrickStateStore {

	void writeObjectFor(Class<?> brick, Object object);
	Object readObjectFor(Class<?> brick, ClassLoader classloader);
	
}
