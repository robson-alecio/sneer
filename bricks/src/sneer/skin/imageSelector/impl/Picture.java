package sneer.skin.imageSelector.impl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.LabelUI;

class Picture extends JLabel{

	private static final long serialVersionUID = 1L;
	private final AvatarPreview _avatarPreview;
	
	Picture(AvatarPreview avatarPreview) {
		_avatarPreview = avatarPreview;
		addModelChangeListeners();
	}

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
	
	@Override
	public void setUI(final LabelUI old) {
		LabelUI wrapper = new LabelUI(){

			@Override
			public void paint(Graphics g, JComponent c) {
				old.paint(g, c);
		        g.setColor(new Color(50, 50, 50, 150));
		        		        
		        int top = _avatarPreview.getTopCropLocation();
		        int botton = _avatarPreview.getBottonCropLocation();
		        int left = _avatarPreview.getLeftCropLocation();
		        int right = _avatarPreview.getRightCropLocation();

		        g.fillRect(0, 0, c.getWidth(), top);
				g.fillRect(0, botton, c.getWidth(), c.getHeight());
				g.fillRect(0, top, left, botton-top);
				g.fillRect(right, top, c.getWidth(), botton-top);
		        
		    }
		};
		super.setUI(wrapper);
	}
	
	private void addModelChangeListeners() {
		ChangeListener changeListener = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				repaint();
			}
		};
		_avatarPreview._top.getModel().addChangeListener(changeListener);
		_avatarPreview._botton.getModel().addChangeListener(changeListener);
		_avatarPreview._left.getModel().addChangeListener(changeListener);
		_avatarPreview._right.getModel().addChangeListener(changeListener);
		_avatarPreview._cropCheck.getModel().addChangeListener(changeListener);
	}
}
