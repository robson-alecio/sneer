package sneer.bricks.softwaresharing;

import java.util.List;

public interface BrickInfo {

	String name();
	
	boolean isSnapp();
	
	List<BrickVersion> versions();

}
