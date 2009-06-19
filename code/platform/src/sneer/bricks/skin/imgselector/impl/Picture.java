package sneer.bricks.skin.imgselector.impl;

import java.awt.Dimension;

import javax.swing.JLabel;

class Picture extends JLabel{

	private static final long serialVersionUID = 1L;
	
	Picture() {}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(getIcon().getIconWidth(),
							 getIcon().getIconHeight());
	}
	
	@Override
	public void setLocation(int x, int y) {
		super.setLocation(0, 0);
	};
	
	@Override
	public java.awt.Point getLocation() {
		super.setLocation(0, 0);
		return super.getLocation();
	};
}
