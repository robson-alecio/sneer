package sneer.bricks.softwaresharing.publisher.impl;

import sneer.bricks.softwaresharing.publisher.BrickPublisher;
import sneer.foundation.lang.exceptions.NotImplementedYet;

class BrickPublisherImpl implements BrickPublisher {

	@Override
	public void publishBrick(String brickName) {
		throw new NotImplementedYet();
//		File brickFolder = brickFolderFor(brickName);
//		Sneer1024 hash = my(FilePublisher.class).publish(brickFolder);
//		my(TupleSpace.class).publish(new Building(hash));
	}

	
//	private File brickFolderFor(String brickName) {
//		String brickFolder = packageFor(brickName).replace('.', File.separatorChar);
//		
//		File ownBrickFolder = new File(my(FolderConfig.class).ownSrcFolder().get(), brickFolder);
//		if (ownBrickFolder.exists())
//			return ownBrickFolder;
//		
//		File platformBrickFolder = new File(my(FolderConfig.class).platformSrcFolder().get(), brickFolder);
//		if (platformBrickFolder.exists())
//			return platformBrickFolder;
//		
//		throw new IllegalStateException("Brick not found: " + brickName);
//	}
//
//
//	private String packageFor(String brickName) {
//		return my(Lang.class).strings().substringBeforeLast(brickName, ".");
//	}

}
