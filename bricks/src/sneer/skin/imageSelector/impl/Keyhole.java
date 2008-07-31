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
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.border.BevelBorder;

import sneer.skin.image.ImageFactory;

public class Keyhole extends JComponent {
    private static final long serialVersionUID = 1L;
	private AvatarPreview _avatarPreview;
	private Point _mouseLocation;
	private Point _layeredPaneLocation;
	private Robot robot;
	private final ImageFactory _imageFactory;
	
    public Keyhole(JLayeredPane layeredPane, AvatarPreview avatarPreview, ImageFactory imageFactory) {
		_avatarPreview = avatarPreview;
		_imageFactory = imageFactory;
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

	private void captureAvatar() {
		BufferedImage buffer = getHoleSorceImage();
		List<MyLabel> avatars = _avatarPreview._avatarList;
		for (MyLabel avatar : avatars) {
			ImageIcon icon = new ImageIcon(
				_imageFactory.getScaledInstance(buffer, (int)avatar._size.getWidth(), (int)avatar._size.getHeight()));
			avatar.setIcon(icon);
		}
	}

	private BufferedImage getHoleSorceImage() {
		Point holeLocation = getLocationOnScreen();
		BufferedImage buffer = robot.createScreenCapture(
			new Rectangle(holeLocation.x+2, holeLocation.y+2,
						  getWidth()-4, getHeight()-4));
		return buffer;
	}

	private void addMouseListeners(final JLayeredPane layeredPane) {
		MouseAdapter clickListener = new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				captureAvatar();
			}
		};
		addMouseListener(clickListener);
		layeredPane.addMouseListener(clickListener);
		
		MouseMotionAdapter mouseMotionListener = new MouseMotionAdapter() {
		    @Override
			public void mouseMoved(MouseEvent e) {
		       _mouseLocation = e.getLocationOnScreen();
		       _layeredPaneLocation = layeredPane.getLocationOnScreen();
		       setTrueLocation();
		    }
		};
		
		layeredPane.addMouseMotionListener(mouseMotionListener);		
		addMouseMotionListener(mouseMotionListener);		
				
		layeredPane.addMouseWheelListener(
			new MouseWheelListener(){
				@Override
				public void mouseWheelMoved(MouseWheelEvent e) {
					int notches = e.getWheelRotation() * 5;
					int size = getPreferredSize().width - notches;
					if (size < 24)
						size = 24;
					
					setPreferredSize(new Dimension(size, size));
					setTrueLocation();
					getParent().validate();
				}
			}
		);
	}
	
	private void setTrueLocation() {
		int ix = _mouseLocation.x-_layeredPaneLocation.x-getWidth();
		int iy = _mouseLocation.y-_layeredPaneLocation.y-getHeight();
		if(ix<0)ix=0;
		if(iy<0)iy=0;
		super.setLocation(ix, iy);
	};
	
	@Override
	public void setLocation(int x, int y) {
		//ignore
	};
}