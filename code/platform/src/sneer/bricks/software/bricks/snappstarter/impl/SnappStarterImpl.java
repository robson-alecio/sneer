package sneer.bricks.software.bricks.snappstarter.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import sneer.bricks.software.bricks.finder.BrickFinder;
import sneer.bricks.software.bricks.snappstarter.Snapp;
import sneer.bricks.software.bricks.snappstarter.SnappStarter;

class SnappStarterImpl implements SnappStarter {

	private final ClassLoader _apiClassLoader = SnappStarter.class.getClassLoader();
	private final Collection<Object> _referenceToAvoidGC = new ArrayList<Object>();

	@Override
	public void startSnapps() {
		try {
			tryToStartSnapps();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	private void tryToStartSnapps() throws IOException, ClassNotFoundException {
		for (String brickName : my(BrickFinder.class).findBricks()) {
			Class<?> brick = _apiClassLoader.loadClass(brickName);
			if (isSnapp(brick)) startAndKeep(brick);
		}
	}

	private void startAndKeep(Class<?> brick) {
		_referenceToAvoidGC.add(my(brick));
	}

	private boolean isSnapp(Class<?> brick) {
		return brick.getAnnotation(Snapp.class) != null;
	}

}
