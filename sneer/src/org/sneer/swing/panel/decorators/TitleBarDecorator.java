package org.sneer.swing.panel.decorators;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import org.sneer.swing.panel.TransparentPanel;

public class TitleBarDecorator implements Decorator {

	TransparentPanel pnl;
	String title;
	
	public TitleBarDecorator(TransparentPanel pnl, String title){
		this.pnl = pnl;
		this.title=title;
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING , 
												RenderingHints.VALUE_ANTIALIAS_ON));
        int w = pnl.getWidth();
        int h = pnl.getHeight(); 
		
		g2.setPaint(new GradientPaint(new Point(0,0),Color.black,new Point(w,0),Color.blue));
		AlphaComposite ac =  AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f);
		g2.setComposite(ac);
		g2.fillRect(0, 0, w, h);
		g2.setPaint(new GradientPaint(new Point(0,0),Color.white,new Point(0,h),Color.black));
		g2.fillRect(0, 0, w, h);
	}

}
