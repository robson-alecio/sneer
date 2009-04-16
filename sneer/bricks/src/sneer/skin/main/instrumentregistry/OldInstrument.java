package sneer.skin.main.instrumentregistry;

import sneer.skin.GuiBrick;
import sneer.skin.main.dashboard.InstrumentPanel;

public interface OldInstrument extends GuiBrick {

	static int DEFAULT_HEIGHT = 50;
	
	void init(InstrumentPanel container);
	int defaultHeight();
	String title();
}
