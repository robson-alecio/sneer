package sneer.bricks.software.folderconfig;

import java.io.File;

import sneer.bricks.hardware.ram.ref.immutable.Immutable;
import sneer.foundation.brickness.Brick;

@Brick
public interface FolderConfig {

	Immutable<File> ownBinFolder();
	Immutable<File> platformBinFolder();

	Immutable<File> ownSrcFolder();
	Immutable<File> platformSrcFolder();

	Immutable<File> dataFolder();
	File getStorageFolderFor(Class<?> brick);

	Immutable<File> logFile();

}
