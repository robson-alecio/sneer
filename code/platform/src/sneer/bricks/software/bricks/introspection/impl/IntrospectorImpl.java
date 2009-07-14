package sneer.bricks.software.bricks.introspection.impl;

import sneer.bricks.software.bricks.introspection.Introspector;
import sneer.foundation.brickness.Brick;

class IntrospectorImpl implements Introspector {

	@Override
	public Class<?> brickInterfaceFor(Object brick) {
		if (null == brick)
			throw new IllegalArgumentException("brick");
		
		for (Class<?> intrface : brick.getClass().getInterfaces())
			if (intrface.isAnnotationPresent(Brick.class))
				return intrface;
		
		throw new IllegalArgumentException(brick + " is not a brick.");
	}

}
