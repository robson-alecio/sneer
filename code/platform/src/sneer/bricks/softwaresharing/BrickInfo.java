package sneer.bricks.softwaresharing;

import java.util.List;

public interface BrickInfo {
	
	public enum Status {
		DIVERGING,
		DIFFERENT,
		NEW,
		CURRENT, 
		REJECTED
	}
	
	String name();
	
	List<BrickVersion> versions();
	void setStagedForExecution(BrickVersion version, boolean staged);

	Status status();
	boolean isSnapp();
}
