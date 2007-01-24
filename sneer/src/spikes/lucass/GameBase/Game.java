package spikes.lucass.GameBase;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import spikes.lucass.GameBase.GameTypes.GameOptions;

public class Game implements MouseListener, MouseMotionListener{		
	public static final int EMPTY= -1;
	
	private Board _board;
	private PieceSet _pieceSet;

	private PieceSprite _movingPiece= newInvisiblePiece();

	
//	private void debug_printSaveState() {
//		for(int i=0; i<_boardModel.length; i++){
//			for(int j=0; j<_boardModel[i].length; j++){
//				System.out.print(" - " + _boardModel[i][j]);
//			}
//			System.out.println();
//		}
//	}

	public Game(GameOptions gameOptions){
		//creates board
		_board= new Board(gameOptions.getBoardImage());
		_board.generateBoard(gameOptions.getColNumber(), gameOptions.getRowNumber(), gameOptions.getBoardCellVariation());
		
		//creates pieceSet
		_pieceSet= new PieceSet(_board, gameOptions);
	}
	
//	public void saveBoardState(){		
//		for(int i=0; i<_boardModel.length; i++){
//			System.arraycopy(_boardModel[i], 0, _tmpBoardModel[i], 0, _boardModel[i].length);
//		}
//	}

//	public void loadBoardState(/*BoardState bs*/){
//		createAndArrangePieces(_tmpBoardModel);
//	}
	
	public Board getBoard(){
		return _board;
	}
	
	public void paint(Graphics g){
		_board.paint(g);
		_pieceSet.paint(g);
		_movingPiece.paint(g);
	}

	public void mouseClicked(MouseEvent e) {
//		if(e.getButton()==MouseEvent.BUTTON1){
//			saveBoardState();
//		}else{
//			loadBoardState();
//		}
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	public void mousePressed(MouseEvent e) {
		_movingPiece= _pieceSet.removePieceAtPosition(e.getX(), e.getY());
	}

	public void mouseReleased(MouseEvent e) {
		_movingPiece.setPosition(e.getX(), e.getY());
		_pieceSet.insertPieceIfOnTheBoard(_movingPiece);
		_movingPiece= newInvisiblePiece();
	}

	private PieceSprite newInvisiblePiece() {
		PieceSprite p= new PieceSprite(null,0,0,0);
		p.setVisible(false);
		return p;
	}

	public void mouseDragged(MouseEvent e) {
		_movingPiece.setPosition(e.getX(), e.getY());
	}

	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
	}
}
