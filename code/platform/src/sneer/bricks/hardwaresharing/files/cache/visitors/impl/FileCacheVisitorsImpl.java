package sneer.bricks.hardwaresharing.files.cache.visitors.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardwaresharing.files.cache.FileCache;
import sneer.bricks.hardwaresharing.files.cache.visitors.FileCacheVisitor;
import sneer.bricks.hardwaresharing.files.cache.visitors.FileCacheVisitors;
import sneer.bricks.hardwaresharing.files.protocol.FolderContents;
import sneer.bricks.hardwaresharing.files.protocol.FolderEntry;
import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.foundation.lang.exceptions.NotImplementedYet;

public class FileCacheVisitorsImpl implements FileCacheVisitors {

	@Override
	public void accept(Sneer1024 startingPoint, FileCacheVisitor visitor) {
		
		Object contents = my(FileCache.class).getContents(startingPoint);
		if (contents == null) throw new NotImplementedYet();
		
		if (contents instanceof FolderContents)
			visitFolderContents(visitor, (FolderContents)contents);
		else
			visitor.visitFileContents((byte[])contents);
	}

	
	private void visitFolderContents(FileCacheVisitor visitor, FolderContents contents) {
		
		visitor.visitFolder();
		
		for (FolderEntry entry : contents.contents) {
			visitor.enterFolderEntry(entry.name, entry.lastModified);
			accept(entry.hashOfContents, visitor);
			visitor.leaveFolderEntry();
		}
	}
}
