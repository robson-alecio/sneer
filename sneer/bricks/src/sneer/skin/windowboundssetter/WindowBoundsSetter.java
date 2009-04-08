package sneer.skin.windowboundssetter;

import java.awt.Container;
import java.awt.Window;

import sneer.brickness.Brick;

@Brick
public interface WindowBoundsSetter {
	
	void setBestBounds(Window window);
	void setBestBounds(Window window, Container container);
	void setBestBounds(Window window, Container container,  int horizontal_limit);

	void setBestBounds(Window window, boolean resizeHeight);
	void setBestBounds(Window window, Container container, boolean resizeHeight);
	void setBestBounds(Window window, Container container, boolean resizeHeight, int horizontal_limit);

	void defaultContainer(Container container);
}
