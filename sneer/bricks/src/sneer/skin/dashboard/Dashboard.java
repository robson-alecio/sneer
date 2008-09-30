package sneer.skin.dashboard;

import java.awt.Container;

public interface Dashboard{

	Container getRootPanel();
	Container getContentPanel();
	void moveInstrument(int index, InstrumentWindow frame);
	void moveInstrumentUp(InstrumentWindow frame);
	void moveInstrumentDown(InstrumentWindow frame);
	
}
