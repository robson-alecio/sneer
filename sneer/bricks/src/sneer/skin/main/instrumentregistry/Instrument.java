package sneer.skin.main.instrumentregistry;

import sneer.brickness.Brick;
import sneer.skin.main.dashboard.InstrumentWindow;

@Brick
public interface Instrument {

	static int DEFAULT_HEIGHT = 30;
	
	void init(InstrumentWindow container);
	int defaultHeight();
	String title();
}
