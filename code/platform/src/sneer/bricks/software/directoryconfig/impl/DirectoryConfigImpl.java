package sneer.bricks.software.directoryconfig.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;

import sneer.bricks.hardware.ram.ref.immutable.Immutable;
import sneer.bricks.hardware.ram.ref.immutable.Immutables;
import sneer.bricks.software.directoryconfig.DirectoryConfig;


public class DirectoryConfigImpl implements DirectoryConfig {

	private final Immutable<File> _ownBinDirectory = immutable();
	private final Immutable<File> _platformBinDirectory = immutable();
	private final Immutable<File> _ownSrcDirectory = immutable();
	private final Immutable<File> _platformSrcDirectory = immutable();
	private final Immutable<File> _dataDirectory = immutable();
	private final Immutable<File> _logFile = immutable();

	@Override
	public Immutable<File> ownBinDirectory() {
		return _ownBinDirectory;
	}

	@Override
	public Immutable<File> platformBinDirectory() {
		return _platformBinDirectory;
	}

	@Override
	public Immutable<File> dataDirectory() {
		return _dataDirectory;
	}

	@Override
	public File getStorageDirectoryFor(Class<?> brick) {
		final File directory = new File(dataDirectory().get(), brick.getName().replace(".", "/"));
		directory.mkdirs();
		return directory;
	}

	@Override
	public Immutable<File> logFile() {
		return _logFile;
	}

	private static <T> Immutable<T> immutable() {
		return my(Immutables.class).newInstance();
	}

	@Override
	public Immutable<File> ownSrcDirectory() {
		return _ownSrcDirectory;
	}

	@Override
	public Immutable<File> platformSrcDirectory() {
		return _platformSrcDirectory;
	}
	
	
}
