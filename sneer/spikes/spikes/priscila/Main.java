package spikes.priscila;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import wheel.lang.Pair;


public class Main extends JFrame{
	
	private static final int _OFFSET = 60;
	private static final int _STONE_DIAMETER = 28;
	private static final long serialVersionUID = 1L;
	public boolean _isBlacksTurn = true;
	private List<Pair<Integer,Integer>> _blackMoves= new ArrayList<Pair<Integer,Integer>>();
	private List<Pair<Integer,Integer>> _whiteMoves= new ArrayList<Pair<Integer,Integer>>();
	private int _scroll;
    
	public Main(){		
		setTitle("Go");
	    setSize(400,400);
	    setResizable(false);
	    setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    addMouseListener(new MouseListener());
	    addMouseMotionListener(new MouseListener());
	}
	
	@Override
	public void paint(Graphics g1){
		Graphics2D g= (Graphics2D)getGraphics();
		
		g.setColor(new Color(228,205,152));
		g.fillRect(0, 0, 400, 400);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		        RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.black);
		for(int i=0; i< 270; i+=30){
			g.drawLine(i+_OFFSET, 0+_OFFSET, i+_OFFSET, 240+_OFFSET);
			g.drawLine(0+_OFFSET, i+_OFFSET, 240+_OFFSET, i+_OFFSET);
		}		
		paintMoves(g, _blackMoves);
		
		g.setColor(Color.white);
		paintMoves(g, _whiteMoves);
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
			System.out.println(e.getY());
			if(e.getY() > 9*30 + _OFFSET) scroll();
			if(e.getX() > 9*30 + _OFFSET) scroll();
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

	private void scroll() {
		_scroll = (_scroll + 1) % 9;
		repaint();
	}
}


