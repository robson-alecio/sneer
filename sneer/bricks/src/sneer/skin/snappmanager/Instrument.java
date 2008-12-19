package sneer.skin.snappmanager;

import sneer.skin.GuiBrick;
import sneer.skin.dashboard.InstrumentWindow;

public interface Instrument extends GuiBrick {

	static int DEFAULT_HEIGHT = 25;
	
	void init(InstrumentWindow container);
	int defaultHeight();
}
