package sneer.skin.snappmanager;

import sneer.skin.GuiBrick;
import sneer.skin.olddashboard.InstrumentWindow;

public interface Instrument extends GuiBrick {

	static int DEFAULT_HEIGHT = 50;
	
	void init(InstrumentWindow container);
	int defaultHeight();
	String title();
}
