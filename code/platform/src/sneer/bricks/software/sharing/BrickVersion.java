package sneer.bricks.software.sharing;

import java.util.List;

public interface BrickVersion {

	public enum Status {
		CURRENT, 
		DIFFERENT,
		REJECTED
	}
	
	Status status();
	boolean isStagedForExecution();
	void setStagedForExecution(boolean staged);
	void setRejected(boolean rejected);
	
	long publicationDate();
	
	List<String> knownUsers();
	int unknownUsers();
	
	List<FileVersion> files();
}
