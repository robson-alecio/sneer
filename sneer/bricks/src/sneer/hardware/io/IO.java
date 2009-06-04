package sneer.hardware.io;

import java.io.File;

import sneer.brickness.Brick;

@Brick
public interface IO {

	Files files();
	
	interface Files{
		boolean isEmpty(File file);
		File concat(File basePath, String path);
		File concat(String basePath, String path);
	}
}
