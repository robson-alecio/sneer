package sneer.skin.dashboard;

import java.awt.Container;

public interface Dashboard{

	Container getRootPanel();
	Container getContentPanel();
	void moveSnapp(int index, InstrumentWindow frame);
	void moveSnappUp(InstrumentWindow frame);
	void moveSnappDown(InstrumentWindow frame);
	
}
