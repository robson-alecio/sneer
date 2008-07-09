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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

public class Keyhole extends JComponent {
    private static final long serialVersionUID = 1L;
    private JLayeredPane _layeredPane;
    private BufferedImage buffer;

    public Keyhole(JLayeredPane layeredPane) {
    	_layeredPane = layeredPane;
        addMouseListeners(layeredPane);
        addBufferListener();
    }   
    
	private void addBufferListener() {
		addComponentListener(new ComponentAdapter() {
            @Override
			public void componentHidden(ComponentEvent componentEvent) {
            	buffer = null;
            }
            @Override
			public void componentResized(ComponentEvent componentEvent) {
            	buffer = null;
            }
        });
	}

	private void addMouseListeners(JLayeredPane layeredPane) {
		layeredPane.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
			public void mouseMoved(MouseEvent e) {
                Point location = e.getPoint();
                location.translate(-getWidth() / 2, -getHeight() / 2);
                setLocation(location);
            }
        });
		
		layeredPane.addMouseListener(
			new MouseAdapter(){
				@Override
				public void mouseEntered(MouseEvent arg0) {
					SwingUtilities.invokeLater(
						new Runnable() {
							@Override
							public void run() {
								setBorder(new BevelBorder(BevelBorder.LOWERED));
							}
						}
					);	
				}
	
				@Override
				public void mouseExited(MouseEvent arg0) {
			    	setBorder(null);
				}
			}
		);		
	}
   
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(128,128);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        if (buffer == null) {
            buffer = createBuffer();
        }
        
        Graphics2D g2 = buffer.createGraphics();
        g2.setComposite(AlphaComposite.Clear);
        g2.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
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
