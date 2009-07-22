package sneer.bricks.softwaresharing.gui.impl;

import sneer.bricks.softwaresharing.FileVersion;

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
