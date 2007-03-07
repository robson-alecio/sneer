package spikes.sandro.swing.panel;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;

import spikes.sandro.swing.layout.StackLayout;

public class GradientPanel extends JPanel {
	
	public static final int _TOP_DOWN = 0;
	public static final int _TOP_CENTER_DONW = 1;
	
	private static final long serialVersionUID = 1L;
	
	protected Color gradientStart = new Color(182, 219, 136);//220, 255, 149);
	protected Color gradientEnd = new Color(158, 211, 102);//183, 234, 98);
	protected int type = _TOP_CENTER_DONW;
	
	public GradientPanel(){}
	
	public GradientPanel(Color start, Color end) {
		this.gradientStart = start;
		this.gradientEnd = end;
	}

	public GradientPanel(Color start, Color end, int tp) {
		this(start,end);
		this.type=tp;
	}

	public GradientPanel(int tp) {
		this.type=tp;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
        int w = getWidth();
        int h = getHeight(); 

        Graphics2D g2 = (Graphics2D)g;
		Paint oldPainter = g2.getPaint();

		if(_TOP_DOWN==type){
	        Point point  = new Point(0,0);
	        Point point2 = new Point(0,h);
	        g2.setPaint(new GradientPaint(point,  gradientStart,
	        							  point2, gradientEnd));
	        g2.fillRect(0,0,w,h);
	        
		}else if(_TOP_CENTER_DONW==type){
			GradientPaint painter = new GradientPaint(0, h/2, gradientEnd, 
													  0, h,   gradientStart);
			g2.setPaint(painter);
			g2.fillRect(0,(h/2),w, h);

			painter = new GradientPaint(0, 0,   gradientStart, 
										0, h/2, gradientEnd);
			g2.setPaint(painter);
			g2.fillRect(0,0,w,h/2);
		}
		g2.setPaint(oldPainter);		
	}
	
	public static void main(String[] args){	
		
		JFrame frame1 = new JFrame();		
		GradientPanel tp1 = new GradientPanel(Color.black,Color.darkGray, GradientPanel._TOP_DOWN);			
		frame1.getContentPane().add(tp1);
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		frame1.setBounds(50, 50, 200, 200);
		frame1.setVisible(true);
		
		JFrame frame2 = new JFrame();		
		GradientPanel tp2 = new GradientPanel(Color.black,Color.darkGray, GradientPanel._TOP_CENTER_DONW);	
		frame2.getContentPane().setLayout(new StackLayout());		
		frame2.getContentPane().add(tp2, StackLayout.BOTTOM);
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		frame2.setBounds(250, 50, 200, 200);
		frame2.setVisible(true);
		
	}		
}
