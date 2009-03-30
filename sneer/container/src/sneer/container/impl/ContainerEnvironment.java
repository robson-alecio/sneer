package sneer.container.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sneer.commons.environments.Environment;

class ContainerEnvironment implements Environment {

	private final List<Object> _bricks = Collections.synchronizedList(new ArrayList<Object>());
	
	@Override
	public <T> T provide(Class<T> intrface) {
		for (Object candidate : _bricks.toArray())
			if (intrface.isInstance(candidate))
				return (T)candidate;
		return null;
	}

	void bind(Object brickImpl) {
		_bricks.add(brickImpl);
	}

}
