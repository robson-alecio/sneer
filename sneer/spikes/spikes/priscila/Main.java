package spikes.priscila;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import wheel.lang.Pair;
import wheel.lang.Threads;


public class Main extends JFrame{
	
	public class Scroller implements Runnable {

		public void run() {
			while(true){
				if (!_shouldScrollY) continue;
				_scroll = (_scroll + 1) % 9;
				repaint();
				Threads.sleepWithoutInterruptions(300);
			}
		}

	}

	private static final int _OFFSET = 60;
	private static final int _STONE_DIAMETER = 28;
	private static final long serialVersionUID = 1L;
	public boolean _isBlacksTurn = true;
	private List<Pair<Integer,Integer>> _blackMoves= new ArrayList<Pair<Integer,Integer>>();
	private List<Pair<Integer,Integer>> _whiteMoves= new ArrayList<Pair<Integer,Integer>>();
	private int _scroll;
    private BufferedImage bi;
	private volatile boolean _shouldScrollY;
    
	
	public Main(){		
		setTitle("Go");
	    setSize(400,400);
	    setResizable(false);
	    setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    addMouseListener(new MouseListener());
	    addMouseMotionListener(new MouseListener());
	    
	    Threads.startDaemon(new Scroller());
	}
	
	@Override
	public void paint(Graphics g1){
		Graphics2D buffer = getBuffer();

	    buffer.setColor(Color.white);
		buffer.fillRect(0, 0, 400, 400);

		buffer.setColor(new Color(228,205,152));
		buffer.fillRect(0+ _OFFSET, 0+_OFFSET, 240, 240);
	    
		buffer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		buffer.setColor(Color.black);
		for(int i=0; i< 270; i+=30){
			buffer.drawLine(i+_OFFSET, 0+_OFFSET, i+_OFFSET, 240+_OFFSET);
			buffer.drawLine(0+_OFFSET, i+_OFFSET, 240+_OFFSET, i+_OFFSET);
		}	
		
		buffer.setColor(Color.black);
		paintMoves(buffer, _blackMoves);
		buffer.setColor(Color.white);
		paintMoves(buffer, _whiteMoves);		

		g1.drawImage(bi, 0, 0, this);
	}

	private Graphics2D getBuffer() {
		if (bi == null) {
	    	bi = (BufferedImage)createImage(400, 400);
	    	return bi.createGraphics();
	    }
		return (Graphics2D)bi.getGraphics();
	}

	private void paintMoves(Graphics2D g, List<Pair<Integer, Integer>> moves) {
		
		for(Pair<Integer, Integer> move : moves) {
			int x = move._a;
			int y = move._b + ((_scroll * 30) % (9 * 30));
			g.fillOval(x, y, _STONE_DIAMETER, _STONE_DIAMETER);	
		}
	}
	
	public static void main(String[] args){
    	new Main();
	}
	
	private class MouseListener extends MouseAdapter{
		
		@Override
		public void mouseMoved(MouseEvent e) {
			_shouldScrollY = e.getY() > 9*30 + _OFFSET;
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			
			int x = ((e.getX()+15)/30)*30 - (_STONE_DIAMETER / 2);
			int y = ((e.getY()+15)/30)*30 - (_STONE_DIAMETER / 2);
			
			(_isBlacksTurn ? _blackMoves : _whiteMoves).add(new Pair<Integer, Integer>(x,y));
			_isBlacksTurn = !_isBlacksTurn;
			
			repaint();		
		}
	}

}


