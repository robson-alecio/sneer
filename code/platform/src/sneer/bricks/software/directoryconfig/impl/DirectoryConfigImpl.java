package sneer.bricks.software.directoryconfig.impl;

import java.io.File;

import sneer.bricks.hardware.ram.ref.immutable.Immutable;
import sneer.bricks.hardware.ram.ref.immutable.Immutables;
import sneer.bricks.software.directoryconfig.DirectoryConfig;
import static sneer.foundation.environments.Environments.my;


public class DirectoryConfigImpl implements DirectoryConfig {

	private final Immutable<File> _ownBinDirectory = my(Immutables.class).newInstance();

	@Override
	public Immutable<File> ownBinDirectory() {
		return _ownBinDirectory;
	}

}
