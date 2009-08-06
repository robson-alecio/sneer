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
		showToVisitor(startingPoint);
	}


	private void showToVisitor(Sneer1024 hashOfContents) {
		Object contents = my(FileCache.class).getContents(hashOfContents);
		if (contents == null) throw new IllegalStateException("Contents not found in " + FileCache.class.getSimpleName());
		
		if (contents instanceof FolderContents)
			showFolderContents((FolderContents)contents);
		else
			showFileContents((byte[])contents);
	}


	private void showFileContents(byte[] contents) {
		_visitor.visitFileContents(contents);
	}

	
	private void showFolderContents(FolderContents contents) {
		
		_visitor.visitFolder();
		
		for (FileOrFolder entry : contents.contents) {
			_visitor.enterFileOrFolder(entry.name, entry.lastModified);
			showToVisitor(entry.hashOfContents);
			_visitor.leaveFileOrFolder();
		}
	}

}
