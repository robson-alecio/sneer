package sneer.skin.imageSelector.impl;
import java.awt.AWTException;
import java.awt.Dimension;
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

public class Keyhole extends JComponent {
    private static final long serialVersionUID = 1L;
	
    private final AvatarPreview _avatarPreview;
	private final JLayeredPane _layeredPane;

	private Point _mouseLocation;
	private Point _layeredPaneLocation;
	private Robot robot;
	
    Keyhole(JLayeredPane layeredPane, AvatarPreview avatarPreview) {
		_layeredPane = layeredPane;
		_avatarPreview = avatarPreview;
    	setBorder(new BevelBorder(BevelBorder.LOWERED));
    	setPreferredSize(new Dimension(128,128));
        addMouseListeners(layeredPane);
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
    }   

	@Override
    public void setPreferredSize(Dimension preferredSize) {
    	super.setPreferredSize(preferredSize);
    	_avatarPreview._area.setValue((int) preferredSize.getHeight());
    	
    }
	
	private BufferedImage getHoleSorceImage() {
		Point holeLocation = getLocationOnScreen();
		BufferedImage buffer = robot.createScreenCapture(
				new Rectangle(holeLocation.x+2, holeLocation.y+2,
						getWidth()-4, getHeight()-4));
		return buffer;
	}

	private void captureAvatar() {
		BufferedImage buffer = getHoleSorceImage();
		_avatarPreview.setAvatar(buffer);
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
		
		Rectangle bounds = getCropWindowBounds();
		if(bounds.width<size){
			size = bounds.width;
		} 
		if(bounds.height<size){
			size = bounds.height;
		} 
		
		setPreferredSize(new Dimension(size, size));
		setTrueLocation();
		getParent().validate();
		return size;
	}	
	
	void setTrueLocation() {
		int x0 = _mouseLocation.x-_layeredPaneLocation.x-getWidth();
		int y0 = _mouseLocation.y-_layeredPaneLocation.y-getHeight();
		
		//verify crop window
		if(_avatarPreview._cropCheck.isSelected()){
			Rectangle bounds = getCropWindowBounds();
			if(!bounds.contains(new Point(x0, bounds.y))){
				x0 = bounds.x;
			}
			if(!bounds.contains(new Point(bounds.x, y0))){
				y0 = bounds.y;
			}
			
			int x1 = _mouseLocation.x-_layeredPaneLocation.x;
			int y1 = _mouseLocation.y-_layeredPaneLocation.y;
			bounds.setSize(bounds.width+bounds.x, bounds.height+bounds.y);
			bounds.setLocation(0,0);
			
			if(!bounds.contains(new Point(x1, bounds.y))){
				x0 = bounds.x+bounds.width-getWidth();
			}
			if(!bounds.contains(new Point(bounds.x, y1))){
				y0 = bounds.y+bounds.height-getHeight();
			}			
		}
		
		//verify window bounds
		if(x0<0)x0=0;
		if(y0<0)y0=0;
		super.setLocation(x0, y0);
	};
	
	@Override
	public void setLocation(int x, int y) {
		//ignore
	};
	
	Rectangle getCropWindowBounds(){
		Rectangle bounds = _layeredPane.getBounds();
		if(!_avatarPreview._cropCheck.isSelected())
			return bounds;
		
		int left = _avatarPreview.getLeftCropLocation();
		int top = _avatarPreview.getTopCropLocation();
		int right = _avatarPreview.getRightCropLocation();
		int botton = _avatarPreview.getBottonCropLocation();
		
		bounds.setBounds(left, top, right-left, botton-top);
		return bounds;
		
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
		
		//show and hide keyhole
		layeredPane.addMouseListener(
			new MouseAdapter(){
				@Override
				public void mouseEntered(MouseEvent arg0) {
					setVisible(true);
				}
	
				@Override
				public void mouseExited(MouseEvent arg0) {
					setVisible(false);
				}
			}
		);

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
		if(_avatarPreview._cropCheck.isSelected()){
			Rectangle bounds = getCropWindowBounds();
			x = bounds.x + x;
			y = bounds.y + y;
		}
		super.setLocation(x, y);
	}
}