package spikes.sandro.old.swing.panel;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

import spikes.sandro.old.image.IconFactory;
import spikes.sandro.old.swing.Util;

import com.jhlabs.image.ShadowFilter;

public class AutoZoomImage extends JLabel {

	private static final long serialVersionUID = 1L;

	public AutoZoomImage(final ImageIcon smallIcon, final ImageIcon bigIcon) {
		this(smallIcon, bigIcon, 0, 0);
	}	
	public AutoZoomImage(final ImageIcon smallIcon, final ImageIcon bigIcon, final int offsetX, final int offsetY) {
		super(smallIcon);	
		
		this.addMouseListener(
				
				
			new MouseListener(){

				JComponent glassPane;	

				public void mouseClicked(MouseEvent e) {}
				public void mousePressed(MouseEvent e) {}
				public void mouseReleased(MouseEvent e) {}
								
				public void mouseExited(MouseEvent e) {
					if(glassPane==null){
						JLabel lb = (JLabel)e.getSource();
						glassPane = Util.getGlassPane(lb);	
					}
					assert glassPane!=null;{
						Component c = glassPane.getParent();
						c.repaint();
					}
				}			

				public void mouseEntered(MouseEvent e) {
					try {
						JLabel c = (JLabel) e.getSource();
						Rectangle r =  c.getBounds();
						if(glassPane==null){
							JLabel lb = (JLabel)e.getSource();
							glassPane = Util.getGlassPane(lb);	
						}
						Graphics g = glassPane.getGraphics();	
						
						Point p1 = c.getLocationOnScreen();
						Point p2 = Util.getWindow(c).getLocationOnScreen();
						int x = (int) (offsetX + p1.x-p2.x - r.getWidth()/2);
						int y = (int) (offsetY + p1.y-p2.y - r.getHeight()/2);				 				

						BufferedImage buf = null;
						ImageIcon icon;
						try{
							buf = IconFactory.createBufferedImage(bigIcon.getImage());
							ShadowFilter f = new ShadowFilter();
							f.setOpacity(.9f);
							f.setDistance(c.getHeight()/5);
							f.setAngle(-0.9f);
							buf = f.filter(buf, null);
							icon = new ImageIcon(buf);
						} catch (Exception ex) {
							icon = bigIcon;
						}
						g.drawImage(icon.getImage(), x, y, null);
					} catch (Exception ex) {
						//ignore
					}

				}
			}
		);
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.anchor= GridBagConstraints.CENTER;
		
		JLabel lb = new AutoZoomImage(IconFactory.getIcon("small/cam.png",true), 
			      					  IconFactory.getIcon("big/cam.png",true),
			      					  0,0);
		lb.setBorder(BorderFactory.createEtchedBorder());
		frame.add(lb,c);
		frame.setBounds(10, 10, 200, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

	}

}
