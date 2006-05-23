package org.sneer.swing.panel.decorators;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import org.sneer.swing.panel.TransparentPanel;


import com.jhlabs.image.GaussianFilter;

public class TransparentDecorator implements Decorator {

	TransparentPanel pnl;
	
	public TransparentDecorator(TransparentPanel pnl){
		this.pnl = pnl;
	}
	
	public void paintComponent(Graphics g) {
		try {
			Graphics2D g2 = (Graphics2D) g;

			Point pos = pnl.getLocationOnScreen();

			BufferedImage buf = new BufferedImage(pnl.getWidth(), pnl.getHeight(), BufferedImage.TYPE_INT_RGB);
			buf.getGraphics().drawImage(TransparentPanel.screenImg, -pos.x, -pos.y, null);

			GaussianFilter gf = new GaussianFilter(pnl.blur);
			BufferedImage img = gf.filter(buf, null);
			g2.drawImage(img, 0, 0, null);

			if(pnl.brightness>0){
				g2.setColor(new Color(255, 255, 255, pnl.brightness));
				g2.fillRect(0, 0, pnl.getWidth(), pnl.getHeight());
			}
		} catch (RuntimeException e) {
			//ignore
		}
	}

}
