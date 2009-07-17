package sneer.bricks.software.sharing;

import java.util.Collection;

public interface BrickVersion {

	public enum Status { CURRENT, DIFFERENT, REJECTED }
	
	Status status();
	boolean isStagedForExecution();
	void setStagedForExecution(boolean staged);
	void setRejected(boolean rejected);
	
	long publicationDate();
	
	Collection<String> knownUsers();
	int unknownUsers();
	
	Collection<FileVersion> files();
}
