package sneer.bricks.softwaresharing.publisher.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;

import sneer.bricks.hardware.cpu.lang.Lang;
import sneer.bricks.hardwaresharing.files.server.FileServer;
import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.bricks.pulp.tuples.TupleSpace;
import sneer.bricks.software.folderconfig.FolderConfig;
import sneer.bricks.softwaresharing.publisher.BrickPublisher;
import sneer.bricks.softwaresharing.publisher.BrickUsage;

class BrickPublisherImpl implements BrickPublisher {

	@Override
	public void publishBrick(String brickName) throws IOException {
		File brickFolder = brickFolderFor(brickName);
		Sneer1024 hash = my(FileServer.class).serve(brickFolder);
		my(TupleSpace.class).publish(new BrickUsage(brickName, hash));
	}

	
	private File brickFolderFor(String brickName) {
		String brickFolder = packageFor(brickName).replace('.', File.separatorChar);
		
		File ownBrickFolder = new File(my(FolderConfig.class).ownSrcFolder().get(), brickFolder);
		if (ownBrickFolder.exists())
			return ownBrickFolder;
		
		File platformBrickFolder = new File(my(FolderConfig.class).platformSrcFolder().get(), brickFolder);
		if (platformBrickFolder.exists())
			return platformBrickFolder;
		
		throw new IllegalStateException("Brick not found: " + brickName);
	}


	private String packageFor(String brickName) {
		return my(Lang.class).strings().substringBeforeLast(brickName, ".");
	}

}
