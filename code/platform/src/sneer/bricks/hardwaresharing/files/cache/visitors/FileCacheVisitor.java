package sneer.bricks.hardwaresharing.files.cache.visitors;

public interface FileCacheVisitor {

	void visitFileOrFolder(String name, long lastModified);

	void enterFolder();
	void leaveFolder();
	
	void visitFileContents(byte[] fileContents);
	
}
