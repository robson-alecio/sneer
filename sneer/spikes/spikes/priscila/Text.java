package spikes.priscila;
/*
 * @(#)Text.java	1.6 98/12/03
 *
 * Copyright 1998 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;


/**
 * The Text class demonstrates clipping an image, lines, text, texture and 
 * gradient with text.
 */
public class Text extends JApplet {


    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Override
	public void init() {
        Demo demo = new Demo();
        getContentPane().add(demo);
        getContentPane().add("North", new DemoControls(demo));
    }


    /** 
     * The Demo class performs the clipping and painting.
     */
    static class Demo extends JPanel {
    
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		static Image img;
        static TexturePaint texture;

        // creates the TexturePaint pattern
        static {
            BufferedImage bi = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
            Graphics2D big = bi.createGraphics();
            big.setBackground(Color.yellow);
            big.clearRect(0,0,5,5);
            big.setColor(Color.red);
            big.fillRect(0,0,3,3);
            texture = new TexturePaint(bi,new Rectangle(0,0,5,5));
        }
        public String clipType = "Lines";
        public boolean doClip = true;  // Clip button selected
    
    
        public Demo() {
            setBackground(Color.white);
            img = getImage("clouds.jpg");
        }
    
    
        public Image getImage(String name) {
            URL url = Text.class.getResource(name);
            Image img1 = getToolkit().getImage(url);
            try {
                MediaTracker tracker = new MediaTracker(this);
                tracker.addImage(img1, 0);
                tracker.waitForID(0);
            } catch (Exception e) {}
            return img1;
        }
    
    
        public void drawDemo(int w, int h, Graphics2D g2) {
	    // creates a TextLayout with the string "JAVA"      
            FontRenderContext frc = g2.getFontRenderContext();
            Font f = new Font("sansserif",Font.BOLD,32);
            String s = new String("JAVA");
            TextLayout tl = new TextLayout(s, f, frc);

            // gets the width and height of the bounds of the Text
            double sw = tl.getBounds().getWidth();
            double sh = tl.getBounds().getHeight();

            double sx = (w-40)/sw;
            double sy = (h-40)/sh;

            /*
             * an AffineTransform that is a scaling transform that scales
             * coordinates sx pixels in the x direction and sy pixels in
             * the y direction
             */          
            AffineTransform Tx = AffineTransform.getScaleInstance(sx, sy);

            // get the outline shape, scaled by Tx
            Shape shape = tl.getOutline(Tx);
            sw = shape.getBounds().getWidth();
            sh = shape.getBounds().getHeight();

            /*
             * creates a new translated shape from the scaled outline shape
             */
            Tx = AffineTransform.getTranslateInstance(w/2-sw/2, h/2+sh/2);
            shape = Tx.createTransformedShape(shape);

            // the bounding rectangle in which to render our objects
            Rectangle r = shape.getBounds();
    
            /*
             * if Clip is selected, set the current clip to the result of
             * the intersection of shape with the current clip.  If this
             * button is selected, the filled pattern or image is only
             * displayed within the bounding rectangle.
             */
            if (doClip) {
                g2.clip(shape);
            }

            if (clipType.equals("Lines")) {
                // if Lines is selected, fill bounds with yellow lines    
                g2.setColor(Color.black);
                g2.fill(r);
                g2.setColor(Color.yellow);
                g2.setStroke(new BasicStroke(1.5f));
                for (int j = r.y; j < r.y + r.height; j=j+3) {
                    g2.drawLine(r.x, j, r.x+r.width, j);
                }
            } else if (clipType.equals("Image")) {
                g2.drawImage(img, r.x, r.y, r.width, r.height, null);
            } else if (clipType.equals("TP")) {
                g2.setPaint(texture);
                g2.fill(r);
            } else if (clipType.equals("GP")) {
                g2.setPaint(new GradientPaint(0,0,Color.blue,w,h,Color.yellow));
                g2.fill(r);
            } else if (clipType.equals("Text")) {

                g2.setColor(Color.black);
                g2.fill(shape.getBounds());
                g2.setColor(Color.cyan);

                // creates a TextLayout from the string "java"
                f = new Font("serif",Font.BOLD,10);
                tl = new TextLayout("java", f, frc);
                sw = tl.getBounds().getWidth();
        
                int x = r.x;
                int y = (int) (r.y + tl.getAscent());
                sh = r.y + r.height;

                /*
                 * draws "java" as long as there is space inside of 
                 * bounding rectangle.
                 */
                while ( y < sh ) {
                    tl.draw(g2, x, y);
                    if ((x += (int) sw) > (r.x+r.width)) {
                        x = r.x;
                        y += (int) tl.getAscent();
                    }
                }
            }

            g2.setClip(new Rectangle(0, 0, w, h));    
            g2.setColor(Color.gray);
            g2.draw(shape);
        }
    
    
        @Override
		public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            Dimension d = getSize();
            g2.setBackground(getBackground());
            g2.clearRect(0, 0, d.width, d.height);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
            drawDemo(d.width, d.height, g2);
        }
    }  // End Demo class 



    /**
     * The DemoControls class provides buttons for showing how a
     * text outline shape clips various objects, which are Lines,
     * Image, TexturePaint, GradientPaint and Text.
     */
    static class DemoControls extends JPanel implements ActionListener {

        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		Demo demo;
        JToolBar toolbar;

        public DemoControls(Demo demo1) {
            this.demo = demo1;
            setBackground(Color.gray);
            add(toolbar = new JToolBar());
            toolbar.setFloatable(false);
            addTool("Clip", true);
            addTool("Lines", true);
            addTool("Image", false);
            addTool("TP", false);
            addTool("GP", false);
            addTool("Text", false);
        }

        public void addTool(String str, boolean state) {
            JButton b = (JButton) toolbar.add(new JButton(str));
            b.setSelected(state);
            b.setBackground(state ? Color.green : Color.lightGray);
            b.addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(toolbar.getComponentAtIndex(0))) {
                JButton b = (JButton) e.getSource();
                b.setSelected(demo.doClip = !demo.doClip);
                b.setBackground(b.isSelected() ? Color.green : Color.lightGray);
            } else {
                for (int i = 1; i < toolbar.getComponentCount(); i++) {
                    JButton b = (JButton) toolbar.getComponentAtIndex(i);
                    b.setBackground(Color.lightGray);
                }
                JButton b = (JButton) e.getSource();
                b.setBackground(Color.green);
                demo.clipType = b.getText();
            }
            demo.repaint();
        }
    } // End DemoControls class


    public static void main(String argv[]) {
        final Text demo = new Text();
        demo.init();
        Frame f = new Frame("Java 2D(TM) Demo - Text");
        f.addWindowListener(new WindowAdapter() {
            @Override
			public void windowClosing(WindowEvent e) {System.exit(0);}
        });
        f.add("Center", demo);
        f.pack();
        f.setSize(new Dimension(400,300));
        f.show();
    }
}