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

	
	FileWritingVisitor(File targetFileOrFolder, long lastModified) {
		_fileOrFolder = targetFileOrFolder;
		_lastModified.push(lastModified);
	}

	
	@Override public void visitFileOrFolder(String name, long lastModified) {
		_fileOrFolder = new File(_fileOrFolder, name);
		_lastModified.push(lastModified);
	}

	
	@Override public void visitFileContents(byte[] fileContents) {
		writeFileTo(_fileOrFolder, fileContents);
		leaveFileOrFolder();
	}
	
	
	@Override public void enterFolder() {
		_fileOrFolder.mkdirs();
	}
	
	
	@Override public void leaveFolder() {
		leaveFileOrFolder();
	}

	
	private void leaveFileOrFolder() {
		setLastModified(_fileOrFolder, _lastModified.pop());
		_fileOrFolder = _fileOrFolder.getParentFile();
	}
	
	
	private void setLastModified(File file, long lastModified) {
		try {
			if (!file.setLastModified(lastModified)) throw new IOException("Unable to set last modified time: " + file);
		} catch (IOException e) {
			throw new sneer.foundation.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	}
	
	private void writeFileTo(File destination, byte[] contents) {
		try {
			my(IO.class).files().writeByteArrayToFile(destination, contents);
		} catch (IOException e) {
			throw new sneer.foundation.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	}
}