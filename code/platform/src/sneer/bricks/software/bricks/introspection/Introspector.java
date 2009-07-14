package sneer.bricks.software.bricks.introspection;

import sneer.foundation.brickness.Brick;

@Brick
public interface Introspector {

	Class<?> brickInterfaceFor(Object brick);

}
