package sneer.skin.dashboard.impl;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JPanel;

import sneer.skin.dashboard.InstrumentWindow;

class InstrumentWindowImpl extends JPanel implements InstrumentWindow {

	private static final long serialVersionUID = 1L;

	@Override
	public Dimension getPreferredSize() {

		int width = getParent().getWidth()-10;
		if(width<0)
			width = 0;
		
		int height = (int) super.getPreferredSize().getHeight();
		if(height<0) height = 30;
		
		Dimension dim = new Dimension(width,height);
		return dim;
	}

	@Override
	public Dimension getSize() {
		int width = getParent().getWidth()-10;
		if(width<0) width = 0;
		
		int height = (int) super.getSize().getHeight();
		
		Dimension dim = new Dimension(width,height);
		return dim;		
	}
	
	@Override
	public Rectangle getBounds() {
		Rectangle bounds = super.getBounds();
		bounds.setLocation(10, (int) bounds.getY());
		return bounds;
	}
	
	@Override
	public Container getContent() {
		return this;
	}
}