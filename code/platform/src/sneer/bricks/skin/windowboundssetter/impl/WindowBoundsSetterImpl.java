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
	private Component _defaultBaseComponent;

	@Override
	public void setDefaultBaseComponet(Component defaultBaseComponent) {
		_defaultBaseComponent = defaultBaseComponent;
		notifyRunnables();
	}

	private void notifyRunnables() {
		for (Runnable runnable : _toRunWhenBaseComponentIsReady) 
			runnable.run();
		_toRunWhenBaseComponentIsReady.clear();
	}

	@Override public void setBestBounds(Window window) { 	setBestBounds(window, _defaultBaseComponent,  0); }
	@Override public void setBestBounds(Window window, Component baseComponent) { setBestBounds(window, (baseComponent!=null)?baseComponent:_defaultBaseComponent, 0); }
	@Override public void setBestBounds(Window window, Component baseComponent, int  horizontalLimit) {
	
		int space = 20;
		
		Point location = defaultLocation(window);
		if(baseComponent!=null){
			try{
				location = baseComponent.getLocationOnScreen();
			}catch (IllegalComponentStateException ignore) {
				//ignore, using default location
			}
		}
		
		int y = location.y;
		int x = location.x;
		
		int width = window.getWidth();
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
		if(_defaultBaseComponent==null)
			return defaultUnusedArea(screenSize);

		Window windowAncestor = SwingUtilities.getWindowAncestor(_defaultBaseComponent);
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
		if(_defaultBaseComponent == null) {
			_toRunWhenBaseComponentIsReady.add(runnable);
			return;
		}
		runnable.run();
	}
}