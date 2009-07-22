package sneer.bricks.software.sharing;

import java.io.File;
import java.util.List;

import sneer.bricks.pulp.crypto.Sneer1024;

public interface BrickVersion {

	public enum Status {
		CURRENT, 
		DIFFERENT,
		REJECTED
	}
	
	Sneer1024 hash();
	
	Status status();
	boolean isStagedForExecution();
	void setStagedForExecution(boolean staged);
	void setRejected(boolean rejected);
	
	long publicationDate();
	
	List<String> knownUsers();
	int unknownUsers();
	
	File sourceFolder();
	List<FileVersion> files();
}
