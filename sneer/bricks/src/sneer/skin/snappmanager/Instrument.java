package sneer.skin.snappmanager;

import java.awt.Container;

import sneer.skin.GuiBrick;

public interface Instrument extends GuiBrick {

	static int ANY_HEIGHT = 0;
	
	void init(Container container);
	int defaultHeight();

}
