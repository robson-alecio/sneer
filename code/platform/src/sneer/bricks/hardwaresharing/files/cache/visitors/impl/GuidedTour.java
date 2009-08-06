package sneer.bricks.hardwaresharing.files.cache.visitors.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardwaresharing.files.cache.FileCache;
import sneer.bricks.hardwaresharing.files.cache.visitors.FileCacheVisitor;
import sneer.bricks.hardwaresharing.files.protocol.FileOrFolder;
import sneer.bricks.hardwaresharing.files.protocol.FolderContents;
import sneer.bricks.pulp.crypto.Sneer1024;

class GuidedTour {
	
	private final FileCacheVisitor _visitor;

	GuidedTour(Sneer1024 startingPoint, FileCacheVisitor visitor) {
		_visitor = visitor;
		showContents(startingPoint);
	}


	private void showContents(Sneer1024 hashOfContents) {
		Object contents = my(FileCache.class).getContents(hashOfContents);
		if (contents == null) throw new IllegalStateException("Contents not found in " + FileCache.class.getSimpleName());
		
		if (contents instanceof FolderContents)
			showFolder((FolderContents)contents);
		else
			showFile((byte[])contents);
	}

	
	private void showFile(byte[] contents) {
		_visitor.visitFileContents(contents);
	}
	

	private void showFolder(FolderContents folderContents) {
		_visitor.enterFolder();
			
		for (FileOrFolder fileOrFolder : folderContents.contents)
			showFileOrFolder(fileOrFolder);

		_visitor.leaveFolder();
	}


	private void showFileOrFolder(FileOrFolder fileOrFolder) {
		_visitor.visitFileOrFolder(fileOrFolder.name, fileOrFolder.lastModified, fileOrFolder.hashOfContents);
		showContents(fileOrFolder.hashOfContents);
	}

}
