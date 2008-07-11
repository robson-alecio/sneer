package sneer.skin.imageSelector.impl;
import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Transparency;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
    private JLayeredPane _layeredPane;
    private BufferedImage _buffer;
	private AvatarPreview _avatarPreview;
	
    public Keyhole(JLayeredPane layeredPane, AvatarPreview avatarPreview) {
    	_layeredPane = layeredPane;
		_avatarPreview = avatarPreview;
    	setBorder(new BevelBorder(BevelBorder.LOWERED));
    	setPreferredSize(new Dimension(128,128));
        addMouseListeners(layeredPane);
        addBufferListener();
    }   
    
    @Override
    public void setPreferredSize(Dimension preferredSize) {
    	super.setPreferredSize(preferredSize);
    	_avatarPreview.area.setValue((int) preferredSize.getHeight());
    	
    }
    
	private void addBufferListener() {
		addComponentListener(new ComponentAdapter() {
            @Override
			public void componentHidden(ComponentEvent componentEvent) {
            	_buffer = null;
            }
            @Override
			public void componentResized(ComponentEvent componentEvent) {
            	_buffer = null;
            }
        });
	}

	private void addMouseListeners(JLayeredPane layeredPane) {
		layeredPane.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
			public void mouseMoved(MouseEvent e) {
                Point location = e.getPoint();
                location.translate(-getWidth() / 2, -getHeight() / 2);
                setTrueLocation(location);
            }
        });
		
		layeredPane.addMouseWheelListener(
			new MouseWheelListener(){
				@Override
				public void mouseWheelMoved(MouseWheelEvent e) {
					int notches = e.getWheelRotation()*5;
					int size = getPreferredSize().width - notches;
					if(size<24)
						size=24;
					
					setPreferredSize(new Dimension(size,size));
					getParent().validate();
				}
			}
		);
	}
	
	private void setTrueLocation(Point location) {
		super.setLocation(location.x, location.y);
	};
	
	@Override
	public void setLocation(int x, int y) {
		//ignore
	};
	
	@Override
    protected void paintComponent(Graphics g) {
        if (_buffer == null) {
            _buffer = createBuffer();
        }
        
        Graphics2D g2 = _buffer.createGraphics();
        g2.setComposite(AlphaComposite.Clear);
        g2.fillRect(0, 0, _buffer.getWidth(), _buffer.getHeight());
        g2.setComposite(AlphaComposite.Src);

        Point location = getLocation();
        location.translate(getWidth() / 2, getHeight() / 2);
        
        int myLayer = JLayeredPane.getLayer(this);
        for (int i = myLayer - 1; i >= 2; i -= 2) {
            Component[] components = _layeredPane.getComponentsInLayer(i);
            for (Component c : components) {
                if (c.getBounds().contains(location)) {
                    g2.translate(c.getX(), c.getY());
                    c.paint(g2);
                    g2.translate(-c.getX(), -c.getY());
                }
            }
        }
        
        g2.dispose();  
    }
    
    private BufferedImage createBuffer() {
        GraphicsEnvironment local = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = local.getDefaultScreenDevice();
        GraphicsConfiguration config = device.getDefaultConfiguration();
        
        Container parent = getParent();
        return config.createCompatibleImage(parent.getWidth(), parent.getHeight(),
                Transparency.TRANSLUCENT);
    }
}
