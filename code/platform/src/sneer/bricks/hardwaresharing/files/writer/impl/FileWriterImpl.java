package sneer.bricks.hardwaresharing.files.writer.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;

import sneer.bricks.hardware.io.IO;
import sneer.bricks.hardwaresharing.files.cache.FileCache;
import sneer.bricks.hardwaresharing.files.protocol.FolderContents;
import sneer.bricks.hardwaresharing.files.protocol.FolderEntry;
import sneer.bricks.hardwaresharing.files.writer.FileWriter;
import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.foundation.lang.exceptions.NotImplementedYet;


public class FileWriterImpl implements FileWriter {

	@Override
	public void writeTo(File fileOrFolder, long lastModified, Sneer1024 hash) throws IOException {
		if (fileOrFolder.exists()) throw new IOException("File to be written already exists: " + fileOrFolder);
		
		File dotPart = prepareDotPart(fileOrFolder);
		doWriteTo(dotPart, lastModified, hash);
		rename(dotPart, fileOrFolder);
	}


	private void doWriteTo(File fileOrFolder, long lastModified, Sneer1024 hash) throws IOException {
		Object contents = my(FileCache.class).getContents(hash);
		if (contents == null) throw new NotImplementedYet();
		
		if (contents instanceof FolderContents)
			writeFolderTo(fileOrFolder, lastModified, (FolderContents)contents);
		else
			writeFileTo(fileOrFolder, lastModified, (byte[])contents);
	}

	
	private void writeFolderTo(File folder, long lastModified, FolderContents contents) throws IOException {
		if (!folder.mkdirs()) throw new IOException("Unable to create folder: " + folder);
		
		for (FolderEntry entry : contents.contents)
			writeFolderEntryTo(folder, entry);
		
		setLastModified(folder, lastModified);
	}


	private void writeFolderEntryTo(File folder, FolderEntry entry) throws IOException {
		doWriteTo(
			new File(folder, entry.name),
			entry.lastModified,
			entry.hashOfContents
		);
	}

	
	private void writeFileTo(File destination, long lastModified, byte[] contents) throws IOException {
		my(IO.class).files().writeByteArrayToFile(destination, contents);
		setLastModified(destination, lastModified);
	}

	
	private File prepareDotPart(File fileOrFolder) throws IOException {
		File result = new File(fileOrFolder.getParent(), fileOrFolder.getName() + ".part");
		my(IO.class).files().forceDelete(result);
		return result;
	}


	private void rename(File file, File newName) throws IOException {
		if (!file.renameTo(newName)) throw new IOException("Unable to rename .part file/folder to actual file/folder: " + file);
	}


	private void setLastModified(File file, long lastModified) throws IOException {
		if (!file.setLastModified(lastModified)) throw new IOException("Unable to set last modified time: " + file);
	}
}
