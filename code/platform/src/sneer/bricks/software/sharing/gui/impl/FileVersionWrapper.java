package sneer.bricks.software.sharing.gui.impl;

import sneer.bricks.software.sharing.FileVersion;

public class FileVersionWrapper {

	private final FileVersion _fileVersion;

	public FileVersionWrapper(FileVersion fileVersion) {
		_fileVersion = fileVersion;
	}
	
	@Override public String toString() {
		return _fileVersion.name();
	}

	public FileVersion fileVersion(){
		return _fileVersion;
	}
}
