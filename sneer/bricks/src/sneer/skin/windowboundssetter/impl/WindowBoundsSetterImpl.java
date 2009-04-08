package sneer.skin.windowboundssetter.impl;
import java.awt.Container;
import java.awt.Point;
import java.awt.Window;

import sneer.skin.windowboundssetter.WindowBoundsSetter;

class WindowBoundsSetterImpl implements WindowBoundsSetter{

	private Container _container;

	@Override
	public void defaultContainer(Container container) {
		_container = container;
	}

	@Override public void setBestBounds(Window window) { 																	setBestBounds(window, _container, false, 0); }
	@Override public void setBestBounds(Window window, Container container) { 									setBestBounds(window, container, false, 0); }
	@Override public void setBestBounds(Window window, Container container, int horizontal_limit) {		setBestBounds(window, container, false, horizontal_limit); }
	@Override public void setBestBounds(Window window, boolean resizeHeight) {									setBestBounds(window, _container, resizeHeight, 0); }
	@Override public void setBestBounds(Window window, Container container, boolean resizeHeight) { 	setBestBounds(window, container, resizeHeight, 0); }
	@Override public void setBestBounds(Window window, Container container, boolean resizeHeight , int  horizontalLimit) {
	
		int space = 20;
		int windowHeight;
		Point location;
		
		windowHeight = window.getHeight();
		if(container==null){
			location = new Point(window.getWidth() + space*2 , 0);
		}else{
			if(resizeHeight) windowHeight = container.getHeight();
			location = container.getLocationOnScreen();
		}
		
		int y = location.y;
		int x = location.x;

		int width = window.getWidth() + space;
		int widthDif = 0;
		if(horizontalLimit != 0 && width > horizontalLimit) 
			widthDif = width - horizontalLimit;
		
		window.setBounds( x - width - space + widthDif, 
									y, 
									width - widthDif, 
									windowHeight);
	}
}