package spikes.sandro.swing.panel;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UnsupportedLookAndFeelException;

import spikes.sandro.image.IconFactory;

public class ImagePanel extends JLabel {

	protected static final long serialVersionUID = 1L;
	protected float alpha = 1.0f;
	protected float scale = 1.0f;
	protected ImageIcon icon;
	
	public ImagePanel(){}
	
	public ImagePanel(String relativePath) {
		this(IconFactory.getIcon(relativePath));
		super.setIcon(icon);
	}

	public ImagePanel(ImageIcon img) {
		this.icon=img;
		this.setMinimumSize(new Dimension(icon.getIconWidth(),
										  icon.getIconWidth()));
		this.setOpaque(false);
		super.setIcon(icon);
	}

	@Override
	protected void paintComponent(Graphics g) {
		try {
			Graphics2D g2 = (Graphics2D) g;
			AlphaComposite ac =  AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
			Composite old = g2.getComposite();
			g2.setComposite(ac);
						
			BufferedImage buf0 = IconFactory.createBufferedImage(icon.getImage());	
			BufferedImage buf1 = IconFactory.getScaledInstance(buf0,
												(int)(icon.getIconWidth()*this.scale),
											    (int)(icon.getIconHeight()*this.scale));
			ImageIcon nii = new ImageIcon(buf1);
			int x0 = (this.getWidth()/2)-(nii.getIconWidth()/2) ;
			int y0 = (this.getHeight()/2)-(nii.getIconHeight()/2);
						
			g2.drawImage(buf1, new AffineTransform(1f,0f,0f,1f, x0,y0), null);
			g2.setComposite(old);		
			
		} catch (Exception e) {
			super.paintComponent(g);
		}
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float palpha) {
		this.alpha = palpha;
		try {
			this.getParent().repaint();
		} catch (Exception e) {
			//ignore
		}
	}

	@Override
	public ImageIcon getIcon() {
		return icon;
	}

	public void setIcon(ImageIcon img) {
		this.icon = img;
		try {
			this.getParent().repaint();
		} catch (Exception e) {
			//ignore
		}
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float pscale) {
		this.scale = pscale;
		try {
			this.getParent().repaint();
		} catch (Exception e) {
			//ignore
		}
	}
	
	public static void main(String args[]) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException{
		JFrame frm = new JFrame();
		ImagePanel img = new ImagePanel("logo128.png");
		frm.add(img);
		frm.setBounds(0, 0, 200, 200);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		frm.setVisible(true);
		
		img.setAlpha(0.5f);
		img.setScale(0.8f);
		
	}


}
