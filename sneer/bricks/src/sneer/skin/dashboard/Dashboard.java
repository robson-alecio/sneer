package sneer.skin.dashboard;

import java.awt.Container;

import sneer.brickness.Brick;

public interface Dashboard extends Brick {

	Container getRootPanel();
	Container getContentPanel();
	void moveInstrument(int index, InstrumentWindow frame);
	void moveInstrumentUp(InstrumentWindow frame);
	void moveInstrumentDown(InstrumentWindow frame);
	
}
