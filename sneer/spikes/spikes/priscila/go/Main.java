package spikes.priscila.go;

import java.awt.Color;
import static java.awt.Color.BLACK;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;


import wheel.lang.Threads;


public class Main extends JFrame{
	
	private static final int _SCREEN_SIZE = 500;
	private static final int _CELL_SIZE = 30;

	public class Scroller implements Runnable {

		public void run() {
			while (true) {
				scrollX();
				scrollY();
				if (_scrollXDelta != 0 || _scrollYDelta != 0) repaint();
				Threads.sleepWithoutInterruptions(150);
			}
		}

		private void scrollX() {
			_scrollX = (_scrollX + _scrollXDelta + 9) % 9;
		}
		
		private void scrollY() {
			_scrollY = (_scrollY + _scrollYDelta + 9) % 9;
		}

	}

	private static final int _MARGIN = 100;
	private static final int _STONE_DIAMETER = 28;
	private static final long serialVersionUID = 1L;

	private final GoBoard _board = new ToroidalGoBoard(9);

	private BufferedImage _bufferImage;

	private volatile int _scrollY;
	private volatile int _scrollX;
	private volatile int _scrollYDelta;
	private volatile int _scrollXDelta;
	
	
	public Main() {		
		setTitle("Go");
	    setSize(_SCREEN_SIZE, _SCREEN_SIZE);
	    setResizable(false);
	    setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	    addMouseListener();
	    
	    Threads.startDaemon(new Scroller());
	}

	private void addMouseListener() {
		MouseListener listener = new MouseListener();
		addMouseListener(listener);
	    addMouseMotionListener(listener);
	}
	
	@Override
	public void paint(Graphics graphics){
		Graphics2D buffer = getBuffer();
		
	    buffer.setColor(new Color(228,205,152,90));
		buffer.fillRect(0, 0, _SCREEN_SIZE, _SCREEN_SIZE);
		
		buffer.setColor(new Color(228,205,152));
		buffer.fillRect(_MARGIN, _MARGIN, _CELL_SIZE * 8, _CELL_SIZE * 8);
	    
		buffer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		buffer.setColor(Color.black);
	
		for(int i = 0; i < 270; i += _CELL_SIZE){
			buffer.drawLine(i+_MARGIN, 0+_MARGIN, i+_MARGIN, 240+_MARGIN);
			buffer.drawLine(0+_MARGIN, i+_MARGIN, 240+_MARGIN, i+_MARGIN);
		}	
		
		paintStones(buffer);

		graphics.drawImage(_bufferImage, 0, 0, this);
	}

	private void paintStones(Graphics2D graphics) {
		int size = _board.size();
		
		for (int x = 0; x < size; x++)
			for (int y = 0; y < size; y++)
				paintStoneOnPosition(graphics, x, y);
	}

	private void paintStoneOnPosition(Graphics2D graphics, int x, int y) {
		Color color = _board.stoneAt(x, y);
		if (color == null) return;
		
		int cx = toCoordinate(scrollX(x));		
		int cy = toCoordinate(scrollY(y));		
		graphics.setColor(color);
		paintStoneOnCoordinates(graphics, cx, cy);
	}

	private int scrollX(int x) { return (x + _scrollX) % 9; }
	private int unscrollX(int x) { return (9 + x - _scrollX) % 9; }

	private int scrollY(int y) { return (y + _scrollY) % 9; }
	private int unscrollY(int y) { return (9 + y - _scrollY) % 9; }
	
	private Graphics2D getBuffer() {
		_bufferImage = (BufferedImage)createImage(_SCREEN_SIZE, _SCREEN_SIZE);
	    return _bufferImage.createGraphics();
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
    	new Main();
	}
	
	private class MouseListener extends MouseAdapter{
		
		@Override
		public void mouseMoved(final MouseEvent e) {
			_scrollXDelta = scrollDeltaFor(e.getX());
			_scrollYDelta = scrollDeltaFor(e.getY());

			repaint();
			SwingUtilities.invokeLater(new Runnable() { @Override public void run() {
				drawTransparentStone(toPosition(e.getX()), toPosition(e.getY()));
			}});
		}

		private int scrollDeltaFor(int coordinate) {
			if (coordinate > 9 * _CELL_SIZE + _MARGIN) return -1;
			if (coordinate < _MARGIN) return 1;
			return 0;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			int x = unscrollX(toPosition(e.getX()));
			int y = unscrollY(toPosition(e.getY()));
			if (!_board.canPlayStone(x, y)) return;

			_board.playStone(x, y);
			repaint();		
		}

	}

	private void drawTransparentStone(int x, int y) {
		Graphics2D graphics = (Graphics2D) getGraphics();
		
		if(_board.nextToPlay() == BLACK)
			graphics.setColor(new Color(0, 0, 0, 50));
		else
			graphics.setColor(new Color(255, 255, 255, 90));
		
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		paintStoneOnCoordinates(graphics, toCoordinate(x), toCoordinate(y));
	}

	private void paintStoneOnCoordinates(Graphics2D graphics, int x, int y) {
		graphics.fillOval(x - (_STONE_DIAMETER / 2), y - (_STONE_DIAMETER / 2), _STONE_DIAMETER, _STONE_DIAMETER);
	}
	
}
