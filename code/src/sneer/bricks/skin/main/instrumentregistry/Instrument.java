package sneer.bricks.skin.main.instrumentregistry;

import sneer.bricks.skin.main.dashboard.InstrumentPanel;

public interface Instrument {

	static int DEFAULT_HEIGHT = 30;
	
	void init(InstrumentPanel container);
	int defaultHeight();
	String title();
}
