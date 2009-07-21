package sneer.bricks.software.sharing;

import java.util.List;

public interface BrickInfo {

	String name();
	
	boolean isSnapp();
	
	List<BrickVersion> versions();

}
