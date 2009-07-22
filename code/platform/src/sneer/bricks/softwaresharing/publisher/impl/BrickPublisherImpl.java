package sneer.bricks.softwaresharing.publisher.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;

import sneer.bricks.hardwaresharing.files.FilePublisher;
import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.bricks.pulp.tuples.TupleSpace;
import sneer.bricks.software.bricks.finder.BrickFinder;
import sneer.bricks.software.directoryconfig.DirectoryConfig;
import sneer.bricks.softwaresharing.publisher.BrickPublisher;
import sneer.bricks.softwaresharing.publisher.BrickUsage;

class BrickPublisherImpl implements BrickPublisher {

	@Override
	public void publishAllBricks() throws IOException {
		for (String brickName : my(BrickFinder.class).findBricks())
			publishBrick(brickName);
	}

	private void publishBrick(String brickName) {
		
		File sourceDirectory = sourceDirectoryFor(brickName);
		
		Sneer1024 hash = my(FilePublisher.class).publishDirectory(sourceDirectory);
		
		my(TupleSpace.class).publish(new BrickUsage(brickName, hash));
		
	}

	private File sourceDirectoryFor(String brickName) {
		String brickDirectory = brickName.replace('.', File.separatorChar);
		
		File ownBrickDirectory = new File(my(DirectoryConfig.class).ownSrcDirectory().get(), brickDirectory);
		if (ownBrickDirectory.exists())
			return ownBrickDirectory;
		
		File platformBrickDirectory = new File(my(DirectoryConfig.class).platformSrcDirectory().get(), brickDirectory);
		if (platformBrickDirectory.exists())
			return platformBrickDirectory;
		
		throw new IllegalStateException();
	}

}
