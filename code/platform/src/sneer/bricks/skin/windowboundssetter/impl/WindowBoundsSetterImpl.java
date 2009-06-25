package sneer.bricks.skin.windowboundssetter.impl;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.IllegalComponentStateException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import sneer.bricks.skin.windowboundssetter.WindowBoundsSetter;

class WindowBoundsSetterImpl implements WindowBoundsSetter{

	private List<Runnable> _toRunWhenBaseComponentIsReady = new ArrayList<Runnable>();
	private Component _component;

	@Override
	public void setDefaultBaseComponet(Component container) {
		_component = container;
		notifyRunnables();
	}

	private void notifyRunnables() {
		for (Runnable runnable : _toRunWhenBaseComponentIsReady) 
			runnable.run();
		_toRunWhenBaseComponentIsReady.clear();
	}

	@Override public void setBestBounds(Window window) { 	setBestBounds(window, _component,  0); }
	@Override public void setBestBounds(Window window, Component component) { setBestBounds(window, (component!=null)?component:_component, 0); }
	@Override public void setBestBounds(Window window, Component component, int  horizontalLimit) {
	
		int space = 20;
		
		Point location = defaultLocation(window);
		if(component!=null){
			try{
				location = component.getLocationOnScreen();
			}catch (IllegalComponentStateException e) {
				//ignore, using default location
			}
		}
		
		int y = location.y;
		int x = location.x;

		int width = window.getWidth() + space;
		int widthDif = 0;
		if(horizontalLimit != 0 && width > horizontalLimit) 
			widthDif = width - horizontalLimit;
		
		x = x - width - space + widthDif;
		width = width - widthDif;
		int height = window.getHeight();

		window.setBounds( x, y, width, height);
	}

	private Point defaultLocation(Window window) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		return new Point((int) ((screenSize.getWidth()+window.getWidth())/2), 
								 (int) ((screenSize.getHeight())/4));
	}

	@Override
	public Rectangle unusedArea() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		if(_component==null)
			return defaultUnusedArea(screenSize);

		Window windowAncestor = SwingUtilities.getWindowAncestor(_component);
		if(windowAncestor==null)
			return defaultUnusedArea(screenSize);
		
		Rectangle used = windowAncestor.getBounds();
		return new Rectangle(0, 0, screenSize.width-used.width, (int)used.getMaxY());
	}

	private Rectangle defaultUnusedArea(Dimension screenSize) {
		return new Rectangle(0,0, screenSize.width, screenSize.height);
	}

	@Override
	public void runWhenBaseContainerIsReady(Runnable runnable) {
		if(_component == null) {
			_toRunWhenBaseComponentIsReady.add(runnable);
			return;
		}
		runnable.run();
	}
}