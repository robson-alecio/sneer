package sneer.hardware.io.file.utils;

import java.io.File;

import sneer.brickness.Brick;

@Brick
public interface FileUtils {

	boolean isEmpty(File file);

	File concat(File basePath, String path);

	File concat(String basePath, String path);

}
