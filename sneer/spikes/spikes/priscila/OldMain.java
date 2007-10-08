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
import javax.swing.SwingUtilities;

import wheel.lang.Pair;
import wheel.lang.Threads;


public class OldMain extends JFrame{
	
	private static final int _SCREEN_SIZE = 500;
	private static final int _CELL_SIZE = 30;

	public class Scroller implements Runnable {

		public void run() {
			while(true){
				if (!_shouldScrollY) continue;
				_scroll = _scroll == 0
					? 8
					: _scroll - 1;
				repaint();
				Threads.sleepWithoutInterruptions(300);
			}
		}

	}

	private static final int _MARGIN = 100;
	private static final int _STONE_DIAMETER = 28;
	private static final long serialVersionUID = 1L;
	public boolean _isBlacksTurn = true;
	private List<Pair<Integer,Integer>> _blackMoves= new ArrayList<Pair<Integer,Integer>>();
	private List<Pair<Integer,Integer>> _whiteMoves= new ArrayList<Pair<Integer,Integer>>();
	private List<Pair<Integer,Integer>> _blackStones= new ArrayList<Pair<Integer,Integer>>();
	private int _scroll;
    private BufferedImage _bufferImage;
	private volatile boolean _shouldScrollY;
	
	public OldMain(){		
		setTitle("Go");
	    setSize(_SCREEN_SIZE,_SCREEN_SIZE);
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
		
	    buffer.setColor(new Color(228,205,152,90));
		buffer.fillRect(0, 0, _SCREEN_SIZE, _SCREEN_SIZE);
		
		buffer.setColor(new Color(228,205,152));
		buffer.fillRect(0+ _MARGIN, 0+_MARGIN, 240, 240);
	    
		buffer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		buffer.setColor(Color.black);
	
		for(int i=0; i< 270; i+=_CELL_SIZE){
			buffer.drawLine(i+_MARGIN, 0+_MARGIN, i+_MARGIN, 240+_MARGIN);
			buffer.drawLine(0+_MARGIN, i+_MARGIN, 240+_MARGIN, i+_MARGIN);
		}	
		
		buffer.setColor(Color.black);
		paintMoves(buffer, _blackMoves);
		buffer.setColor(Color.white);
		paintMoves(buffer, _whiteMoves);		

		g1.drawImage(_bufferImage, 0, 0, this);
	}

	private Graphics2D getBuffer() {
		//if (_bufferImage == null) {
			_bufferImage = (BufferedImage)createImage(_SCREEN_SIZE, _SCREEN_SIZE);
	    	return _bufferImage.createGraphics();
	    //}
		//return (Graphics2D)_bufferImage.getGraphics();
	}

	private void paintMoves(Graphics2D graphics, List<Pair<Integer, Integer>> moves) {
		for(Pair<Integer, Integer> move : moves) {
			int x = toCoordinate(move._a);
			int y = toCoordinate(move._b);
			
			drawStone(graphics, x, y);	
		}
	}

	private int toCoordinate(int position) {
		return position * _CELL_SIZE + _MARGIN;
	}
	
	private int toPosition(int coordinate) {
		int result = (coordinate - _MARGIN + (_CELL_SIZE / 2)) / _CELL_SIZE;
		if (result < 0) return 0;
		if (result > 8) return 8;
		return result;
	}

	public static void main(String[] args){
    	new OldMain();
	}
	
	private class MouseListener extends MouseAdapter{
		
		@Override
		public void mouseMoved(final MouseEvent e) {
			_shouldScrollY = e.getY() > 9*_CELL_SIZE + _MARGIN;
			
			repaint();

			SwingUtilities.invokeLater(new Runnable() { @Override public void run() {
				drawTransparentStone(e);
			}});
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			
			int x = toPosition(e.getX());
			int y = toPosition(e.getY());
		    
			if (!positionIsEmpty(x, y)) return;
			
			verifyPosition(x,y);
			
			(_isBlacksTurn ? _blackMoves : _whiteMoves).add(new Pair<Integer, Integer>(x,y));
			_isBlacksTurn = !_isBlacksTurn;
			
			repaint();		
		}

		private void drawTransparentStone(MouseEvent e) {
			Graphics2D graphics = (Graphics2D) getGraphics();
			if(_isBlacksTurn)
				graphics.setColor(new Color(0, 0, 0, 50));
			else
				graphics.setColor(new Color(255, 255, 255, 90));
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			drawStone(graphics, toCoordinate(toPosition(e.getX())), toCoordinate(toPosition(e.getY())));
		}
	}

	private void drawStone(Graphics2D graphics, int x, int y) {
		graphics.fillOval(x - (_STONE_DIAMETER / 2), y - (_STONE_DIAMETER / 2), _STONE_DIAMETER, _STONE_DIAMETER);
	}

	private boolean positionIsEmpty(int x, int y) {
		
		for(Pair<Integer, Integer> move:_blackMoves){
			if(move._a == x && move._b == y)
				return false;
		}
		for(Pair<Integer, Integer> move:_whiteMoves){
			if(move._a == x && move._b == y)
				return false;
		}
		return true;
	}
	
	private void verifyPosition(final int x, final int y){

			for (Pair<Integer, Integer> move : _blackMoves){
				if ((move._a == x && (move._b == y + 2 || move._b == y - 2))
					|| (move._a == x + 1 && (move._b == y + 1 || move._b == y - 1))
					|| (move._a == x - 1 && (move._b == y + 1 || move._b == y - 1))
					|| ((move._a == x + 2 || move._a == x - 2) && move._b == y )){
					if(!_blackStones.contains(move))
						_blackStones.add(move);
				}
			}

			if(_isBlacksTurn && _blackStones.size() >=3){
				for(Pair<Integer, Integer> moveWhite:_whiteMoves){
					if((moveWhite._a==x && (moveWhite._b == y+1 || moveWhite._b == y-1))
						||(moveWhite._b == y && (moveWhite._a==x+1 || moveWhite._a == x-1))){
						_whiteMoves.remove(moveWhite);
						_blackStones.clear();
						break;
					}
				}
			}
	}
	
}
