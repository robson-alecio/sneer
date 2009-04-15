package sneer.skin.main.instrumentregistry;

import sneer.brickness.Brick;
import sneer.skin.main.dashboard.InstrumentWindow;

@Brick
public interface Instrument {

	static int DEFAULT_HEIGHT = 50;
	
	void init(InstrumentWindow container);
	int defaultHeight();
	String title();
}
