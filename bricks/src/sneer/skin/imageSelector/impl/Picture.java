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
	
	public Picture(AvatarPreview avatarPreview) {
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
		        
		        int top2 = (int) (c.getHeight()*getTop());
		        int botton2 = (int) (c.getHeight()*(1-getBotton()));

		        g.fillRect(0, 0, c.getWidth(), top2);
				g.fillRect(0, botton2, c.getWidth(), c.getHeight());
				
		        g.fillRect(0, top2, (int) (c.getWidth()*getLeft()), botton2-top2);
				g.fillRect((int) (c.getWidth()*(1-getRight())), top2, c.getWidth(), botton2-top2);
		        
		    }

			private double getRight() {
				return getDoubleValue(_avatarPreview.right.getValue());
			}

			private double getLeft() {
				return getDoubleValue(_avatarPreview.left.getValue());
			}

			private double getBotton() {
				return getDoubleValue(_avatarPreview.botton.getValue());
			}

			private double getTop() {
				return getDoubleValue(_avatarPreview.top.getValue());
			}
			
			private double getDoubleValue(double value) {
				if(_avatarPreview.cropCheck.isSelected())
					return (value/10000);
				return 0;
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
		_avatarPreview.top.getModel().addChangeListener(changeListener);
		_avatarPreview.botton.getModel().addChangeListener(changeListener);
		_avatarPreview.left.getModel().addChangeListener(changeListener);
		_avatarPreview.right.getModel().addChangeListener(changeListener);
		_avatarPreview.cropCheck.getModel().addChangeListener(changeListener);
	}
}
