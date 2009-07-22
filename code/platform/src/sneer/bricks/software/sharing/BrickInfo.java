package sneer.bricks.software.sharing;

import java.util.List;

public interface BrickInfo {
	
	public enum Status {
		NEW,
		DIFFERENT,
		CURRENT, 
		REJECTED
	}
	
	String name();
	List<BrickVersion> versions();
	
	Status status();
	boolean isSnapp();
}
