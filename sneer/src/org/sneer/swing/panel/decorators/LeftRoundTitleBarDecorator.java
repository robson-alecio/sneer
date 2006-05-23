package org.sneer.swing.panel.decorators;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import org.sneer.swing.panel.TransparentPanel;

public class LeftRoundTitleBarDecorator implements Decorator {

	TransparentPanel pnl;
	String title;
	
	public LeftRoundTitleBarDecorator(TransparentPanel pnl, String title){
		this.pnl = pnl;
		this.title=title;
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING , 
												RenderingHints.VALUE_ANTIALIAS_ON));
        int w = pnl.getWidth();
        int h = pnl.getHeight(); 
        
		Point p1 = new Point(0,0);
		Point p2 = new Point(w,0);
		
		GradientPaint gradient = new GradientPaint(p1,Color.black,p2,Color.blue);	
		g2.setPaint(gradient);
		
		AlphaComposite ac =  AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f);
		g2.setComposite(ac);
		
		g2.setPaint(gradient);
		g2.fillRect(-1+(int)(h*1.5f)/2, 0, w, h);
		g2.fillArc(0,0, (int) (h*1.5f), (int) (h*1.5f), 180, -90);
		g2.fillRect(0,(int)(h*1.5f)/2,(int)(h*1.5f)/2,h);

		p1 = new Point(0,0);
		p2 = new Point(0,h);
		gradient = new GradientPaint(p1,Color.white,p2,Color.black);	
		g2.setPaint(gradient);

		g2.setPaint(gradient);
		g2.fillRect((int)(h*1.5f)/2, 0, w, h);
		g2.fillArc(0,0, (int) (h*1.5f), (int) (h*1.5f), 180, -90);
		g2.fillRect(0,(int)(h*1.5f)/2,(int)(h*1.5f)/2,h);
	}

}
