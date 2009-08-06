package sneer.bricks.hardwaresharing.files.cache.visitors;

public interface FileCacheVisitor {

	void enterFileOrFolder(String name, long lastModified);

	void visitFolder();
	
	void visitFileContents(byte[] fileContents);
	
	void leaveFileOrFolder();
}
