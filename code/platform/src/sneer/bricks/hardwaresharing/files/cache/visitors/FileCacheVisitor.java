package sneer.bricks.hardwaresharing.files.cache.visitors;

public interface FileCacheVisitor {

	void enterFolderEntry(String entryName, long lastModified);

	void visitFolder();
	
	void visitFileContents(byte[] fileContents);
	
	void leaveFolderEntry();
}
