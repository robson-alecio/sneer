package sneer.skin.dashboard;

import java.awt.Container;

public interface Dashboard{

	Container getRootPanel();
	Container getContentPanel();
	void moveSnapp(int index, SnappFrame frame);
	void moveSnappUp(SnappFrame frame);
	void moveSnappDown(SnappFrame frame);
	
}
