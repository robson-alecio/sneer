package sneer.skin.dashboard.impl;

import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JInternalFrame;

public class SneerInternalFrame extends JInternalFrame {

	private static final long serialVersionUID = 1L;

	public SneerInternalFrame() {
		super();
	}

	public SneerInternalFrame(String _title, boolean _resizable,
			boolean _closable, boolean _maximizable, boolean _iconifiable) {
		super(_title, _resizable, _closable, _maximizable, _iconifiable);
		// setSize(10,10);
		setVisible(true);
	}

	public SneerInternalFrame(String _title) {
		super(_title);
	}

	@Override
	public Dimension getPreferredSize() {

		int width = getParent().getWidth()-10;
		if(width<0) width = 0;
		
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
	public Point getLocation() {
		int x = (int) getParent().getBounds().getMinX()-10;
		int y = (int) super.getBounds().getMinY();
		
		Point dim = new Point(x,y);
		return dim;
	}
	
}
