package sneer.bricks.software.bricks.snappstarter.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import sneer.bricks.software.bricks.finder.BrickFinder;
import sneer.bricks.software.bricks.snappstarter.Snapp;
import sneer.bricks.software.bricks.snappstarter.SnappStarter;
import sneer.bricks.software.directoryconfig.DirectoryConfig;

class SnappStarterImpl implements SnappStarter {

	private final ClassLoader _apiClassLoader = SnappStarter.class.getClassLoader();

	@Override
	public void startSnapps() {
		try {
			tryToStartSnapps();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	private void tryToStartSnapps() throws IOException, ClassNotFoundException {
		for (String brickName : brickNames()) {
			Class<?> brick = _apiClassLoader.loadClass(brickName);
			if (isSnapp(brick)) start(brick);
		}
	}

	private void start(Class<?> brick) {
		my(brick);
	}

	private boolean isSnapp(Class<?> brick) {
		return brick.getAnnotation(Snapp.class) != null;
	}

	private Collection<String> brickNames() throws IOException {
		Collection<String> result = new ArrayList<String>();
		collectBricks(result, my(DirectoryConfig.class).ownBinDirectory().get());
		collectBricks(result, my(DirectoryConfig.class).platformBinDirectory().get());
		return result;
	}

	private void collectBricks(Collection<String> result, File binDirectory) throws IOException {
		result.addAll(my(BrickFinder.class).findBricks(binDirectory));
	}
}
