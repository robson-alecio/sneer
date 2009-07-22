package sneer.bricks.softwaresharing.publisher.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;

import sneer.bricks.hardware.cpu.lang.Lang;
import sneer.bricks.hardwaresharing.files.FileSpace;
import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.bricks.pulp.tuples.TupleSpace;
import sneer.bricks.software.directoryconfig.DirectoryConfig;
import sneer.bricks.softwaresharing.publisher.BrickPublisher;
import sneer.bricks.softwaresharing.publisher.BrickUsage;

class BrickPublisherImpl implements BrickPublisher {

	@Override
	public void publishBrick(String brickName) throws IOException {
		File brickDirectory = brickDirectoryFor(brickName);
		Sneer1024 hash = my(FileSpace.class).publishContents(brickDirectory);
		my(TupleSpace.class).publish(new BrickUsage(brickName, hash));
	}

	
	private File brickDirectoryFor(String brickName) {
		String brickDirectory = packageFor(brickName).replace('.', File.separatorChar);
		
		File ownBrickDirectory = new File(my(DirectoryConfig.class).ownSrcDirectory().get(), brickDirectory);
		if (ownBrickDirectory.exists())
			return ownBrickDirectory;
		
		File platformBrickDirectory = new File(my(DirectoryConfig.class).platformSrcDirectory().get(), brickDirectory);
		if (platformBrickDirectory.exists())
			return platformBrickDirectory;
		
		throw new IllegalStateException("Brick not found: " + brickName);
	}


	private String packageFor(String brickName) {
		return my(Lang.class).strings().substringBeforeLast(brickName, ".");
	}

}
