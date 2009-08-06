package sneer.bricks.softwaresharing;

import java.util.List;

import sneer.bricks.pulp.crypto.Sneer1024;

public interface BrickVersion {

	public enum Status {
		CURRENT, 
		DIFFERENT,
		DIVERGING,
		REJECTED
	}
	
	Sneer1024 hash();
	
	Status status();
	boolean isStagedForExecution();
	void setRejected(boolean rejected);
	
	long publicationDate();
	
	List<String> knownUsers();
	int unknownUsers();
	
	List<FileVersion> files();
}
