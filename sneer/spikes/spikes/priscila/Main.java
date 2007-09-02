package spikes.priscila;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;


public class Main extends JFrame{
	
	private static final int _OFFSET = 60;
	private static final int _STONE_DIAMETER = 26;
	private static final long serialVersionUID = 1L;
    private int x,y=0;
	public boolean _isBlacksTurn = true;
    
	public Main(){		
		setTitle("Go");
	    setSize(400,400);
	    setResizable(false);
	    setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    addMouseListener(new MouseListener());
	}
	
	@Override
	public void paint(Graphics g){
		g.setColor(new Color(228,205,152));
		g.fillRect(0, 0, 400, 400);
		g.setColor(Color.black);
		for(int i=0; i< 270; i+=30){
			g.drawLine(i+_OFFSET, 0+_OFFSET, i+_OFFSET, 240+_OFFSET);
			g.drawLine(0+_OFFSET, i+_OFFSET, 240+_OFFSET, i+_OFFSET);
		}		
	}
	
	public static void main(String[] args){
    	new Main();
	}
	
	private class MouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			
			x = ((e.getX()+15)/30)*30 - (_STONE_DIAMETER / 2);
			y = ((e.getY()+15)/30)*30 - (_STONE_DIAMETER / 2);
					
			Graphics2D g= (Graphics2D)getGraphics();			
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			        RenderingHints.VALUE_ANTIALIAS_ON);
			g.setColor(color());
			_isBlacksTurn = !_isBlacksTurn;
			g.fillOval(x, y, _STONE_DIAMETER, _STONE_DIAMETER);
			
		}

		private Color color() {
			return _isBlacksTurn 
				? Color.black
				: Color.white;
		}
	}
}


