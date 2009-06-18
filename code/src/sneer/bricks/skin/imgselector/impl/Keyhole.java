package sneer.bricks.skin.imgselector.impl;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.border.BevelBorder;

import sneer.foundation.lang.Consumer;


class Keyhole extends JComponent {
    private static final long serialVersionUID = 1L;
	
	private final JLayeredPane _layeredPane;

	private Point _mouseLocation;
	private Point _layeredPaneLocation;
	private Robot _robot;

	private final Consumer<Image> _imageSetter;
	
    Keyhole(JLayeredPane layeredPane, Consumer<Image> imageSetter) {
		_layeredPane = layeredPane;
		_imageSetter = imageSetter;
    	setBorder(new BevelBorder(BevelBorder.LOWERED));
    	setPreferredSize(new Dimension(128,128));
        addMouseListeners(layeredPane);
		try {
			_robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
    }   

	private void onMouseMove(MouseEvent e) {
		_mouseLocation = e.getLocationOnScreen();
		_layeredPaneLocation = _layeredPane.getLocationOnScreen();
		setTrueLocation();
	}
	
	private void onMouseWheel(MouseWheelEvent e) {
		int notches = e.getWheelRotation() * 5;
		int size = getPreferredSize().width - notches;
		tryChangeArea(size);
	}

	int tryChangeArea(int size) {
		if (size < 24)
			size = 24;
		
		setPreferredSize(new Dimension(size, size));
		setTrueLocation();
		getParent().validate();
		return size;
	}	
	
	void setTrueLocation() {
		int x0 = _mouseLocation.x-_layeredPaneLocation.x-getWidth();
		int y0 = _mouseLocation.y-_layeredPaneLocation.y-getHeight();
		
		//verify window bounds
		if(x0<0)x0=0;
		if(y0<0)y0=0;
		super.setLocation(x0, y0);
	};
	
	@Override
	public void setLocation(int x, int y) {
		//ignore
	};
	
	private BufferedImage getHoleSorceImage() {
		Point holeLocation = getLocationOnScreen();
		BufferedImage buffer = _robot.createScreenCapture(
				new Rectangle(holeLocation.x+2, holeLocation.y+2,
						getWidth()-4, getHeight()-4));
		return buffer;
	}

	private void captureAvatar() {
		setVisible(true);
		BufferedImage buffer = getHoleSorceImage();
		_imageSetter.consume(buffer);
	}
	
	private void addMouseListeners(final JLayeredPane layeredPane) {
		//click listener
		MouseAdapter clickListener = new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				captureAvatar();
			}
		};
		addMouseListener(clickListener);
		layeredPane.addMouseListener(clickListener);
		
//		//show and hide keyhole
//		layeredPane.addMouseListener(
//			new MouseAdapter(){
//				@Override
//				public void mouseEntered(MouseEvent arg0) {
//					setVisible(true);
//				}
//	
//				@Override
//				public void mouseExited(MouseEvent arg0) {
//					setVisible(false);
//				}
//			}
//		);

		//mouse move listener
		MouseMotionAdapter mouseMotionListener = new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				onMouseMove(e);
			}
		};
		layeredPane.addMouseMotionListener(mouseMotionListener);
		addMouseMotionListener(mouseMotionListener);

		//MouseWheelListener
		layeredPane.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				onMouseWheel(e);
			}
		});
	}

	void setTrueLocation(int x, int y) {
		super.setLocation(x, y);
	}
}