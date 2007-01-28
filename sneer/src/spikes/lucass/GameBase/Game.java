package spikes.lucass.GameBase;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import spikes.lucass.GameBase.GameTypes.GameOptions;

public class Game{		
	public static final int EMPTY= -1;
	
	private Board _board;
	private PieceSet _pieceSet;

	private PieceSprite _movingPiece= newInvisiblePiece();

	public Game(GameOptions gameOptions){
		_board= new Board(gameOptions.getBoardImage());
		_board.generateBoard(gameOptions.getColNumber(), gameOptions.getRowNumber(), gameOptions.getBoardCellVariation());
		
		_pieceSet= new PieceSet(_board, gameOptions);
	}
	
	public Board getBoard(){
		return _board;
	}
	
	private PieceSprite newInvisiblePiece() {
		PieceSprite p= new PieceSprite(null,0,0,-1);
		p.setVisible(false);
		return p;
	}

	public MouseListener getMouseListener(){
		return new MouseListener(){
			public void mouseClicked(MouseEvent e){}
			public void mouseEntered(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
			
			public void mousePressed(MouseEvent e) {
				PieceSprite tmp= _pieceSet.removePieceAtPosition(e.getX(), e.getY());
				if(tmp!=null)
					_movingPiece= tmp;
			}
			public void mouseReleased(MouseEvent e) {
				if(_movingPiece.getPieceIndex()>=0){
					_movingPiece.setPosition(e.getX(), e.getY());
					_pieceSet.insertPieceIfOnTheBoard(_movingPiece);
					_movingPiece= newInvisiblePiece();
				}
			}
		};
	}
	
	public MouseMotionListener getMouseMotionListener(){
		return new MouseMotionListener(){
			public void mouseMoved(MouseEvent e) {
			}
			public void mouseDragged(MouseEvent e) {
				_movingPiece.setPosition(e.getX(), e.getY());
			}
		};
	}

	public void paint(Graphics g){
		_board.paint(g);
		_pieceSet.paint(g);
		_movingPiece.paint(g);
	}
}
