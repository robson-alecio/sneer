package sneer.bricks.hardwaresharing.files.writer.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;

import sneer.bricks.hardware.io.IO;
import sneer.bricks.hardwaresharing.files.cache.visitors.FileCacheGuide;
import sneer.bricks.hardwaresharing.files.writer.FileWriter;
import sneer.bricks.pulp.crypto.Sneer1024;


public class FileWriterImpl implements FileWriter {

	@Override
	public void writeTo(File fileOrFolder, final long lastModified, Sneer1024 hash) throws IOException {
		if (fileOrFolder.exists()) throw new IOException("File to be written already exists: " + fileOrFolder);
		
		final File dotPart = prepareDotPart(fileOrFolder);
		my(FileCacheGuide.class).guide(new FileWritingVisitor(dotPart, lastModified), hash);
		rename(dotPart, fileOrFolder);
	}
	
	private File prepareDotPart(File fileOrFolder) throws IOException {
		File result = new File(fileOrFolder.getParent(), fileOrFolder.getName() + ".part");
		my(IO.class).files().forceDelete(result);
		return result;
	}


	private void rename(File file, File newName) throws IOException {
		if (!file.renameTo(newName)) throw new IOException("Unable to rename .part file/folder to actual file/folder: " + file);
	}
}
