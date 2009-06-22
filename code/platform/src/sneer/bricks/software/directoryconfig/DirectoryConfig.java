package sneer.bricks.software.directoryconfig;

import java.io.File;

import sneer.bricks.hardware.ram.ref.immutable.Immutable;
import sneer.foundation.brickness.Brick;

@Brick
public interface DirectoryConfig {

	Immutable<File> ownBinDirectory();

	Immutable<File> platformBinDirectory();

}
