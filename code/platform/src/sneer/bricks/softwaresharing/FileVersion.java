package sneer.bricks.softwaresharing;

public interface FileVersion {

	public enum Status { EXTRA, CURRENT, MODIFIED, MISSING }

	String name();
	Status status();

	byte[] contents();
	/** Used for diff comparisons. Returns null if the brick that contains this file is not currently being used (new or deleted brick). Returns the same as contents() if the status is CURRENT. */
	byte[] contentsInCurrentVersion();
	
}
