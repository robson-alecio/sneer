package sneer.bricks.hardwaresharing.files.writer.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;

import sneer.bricks.hardware.io.IO;
import sneer.bricks.hardwaresharing.files.cache.visitors.FileCacheVisitor;

final class FileWritingVisitor implements FileCacheVisitor {
	File _fileOrFolder;
	Deque<Long> _lastModified = new LinkedList<Long>();

	FileWritingVisitor(long lastModified, File targetFileOrFolder) {
		_fileOrFolder = targetFileOrFolder;
		_lastModified.push(lastModified);
	}

	@Override public void visitFolder() {
		_fileOrFolder.mkdirs();
	}
	
	@Override public void enterFolderEntry(String entryName, long lastModified) {
		_fileOrFolder = new File(_fileOrFolder, entryName);
		_lastModified.push(lastModified);
	}

	@Override public void visitFileContents(byte[] fileContents) {
		try {
			writeFileTo(_fileOrFolder, fileContents);
		} catch (IOException e) {
			throw new sneer.foundation.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	}
	
	@Override public void leaveFolderEntry() {
		try {
			setLastModified(_fileOrFolder, _lastModified.pop());
		} catch (IOException e) {
			throw new sneer.foundation.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
		_fileOrFolder = _fileOrFolder.getParentFile();
	}
	
	void setLastModified(File file, long lastModified) throws IOException {
		if (!file.setLastModified(lastModified)) throw new IOException("Unable to set last modified time: " + file);
	}
	
	void writeFileTo(File destination, byte[] contents) throws IOException {
		my(IO.class).files().writeByteArrayToFile(destination, contents);
	}
}