package sneer.skin.old.instrumentregistry;

import sneer.skin.GuiBrick;
import sneer.skin.old.dashboard.OldInstrumentWindow;

public interface OldInstrument extends GuiBrick {

	static int DEFAULT_HEIGHT = 50;
	
	void init(OldInstrumentWindow container);
	int defaultHeight();
	String title();
}
