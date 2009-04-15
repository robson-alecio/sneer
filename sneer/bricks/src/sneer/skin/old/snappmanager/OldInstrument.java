package sneer.skin.old.snappmanager;

import sneer.skin.GuiBrick;
import sneer.skin.old.dashboard.InstrumentWindow;

public interface OldInstrument extends GuiBrick {

	static int DEFAULT_HEIGHT = 50;
	
	void init(InstrumentWindow container);
	int defaultHeight();
	String title();
}
