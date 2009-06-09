package sneer.bricks.skin.main.instrumentregistry;

import sneer.bricks.skin.main.dashboard.InstrumentPanel;
import sneer.foundation.brickness.Brick;

@Brick
public interface Instrument {

	static int DEFAULT_HEIGHT = 30;
	
	void init(InstrumentPanel container);
	int defaultHeight();
	String title();
}
