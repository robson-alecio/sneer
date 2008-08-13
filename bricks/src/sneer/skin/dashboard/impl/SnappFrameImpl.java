package sneer.skin.dashboard.impl;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import sneer.skin.dashboard.SnappFrame;

public class SnappFrameImpl extends JPanel implements SnappFrame {

	private static final long serialVersionUID = 1L;

	public SnappFrameImpl() {
//		setBorder(
//			new CompoundBorder(
//				new BevelBorder(BevelBorder.RAISED),
//				new EmptyBorder(5,5,5,5)));
	}

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
	public Container getContainer() {
		return this;
	}
	
	@Override
	public void setVisible(final boolean visible) {
		SwingUtilities.invokeLater(
			new Runnable(){
				@Override
				public void run() {
					setVisible(visible);
					setVisibleFromSuper(visible);
				}
			}
		);
	}
	
	private void setVisibleFromSuper(boolean visible) {
		super.setVisible(visible);
	}
}
