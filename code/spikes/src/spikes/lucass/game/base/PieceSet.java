package spikes.lucass.game.base;

import java.awt.Graphics;

import spikes.lucass.game.type.GameOptions;


public class PieceSet {
	private PieceSprite[][] _pieces;
	
	private Board _boardInUse;
	
	public PieceSet(Board belongsTo,GameOptions gO) {
		_boardInUse= belongsTo;
		int[][] piecesIds= gO.getDefaultPositions();
		_pieces= new PieceSprite[piecesIds.length][piecesIds[0].length];
		
		for(int row= 0; row<piecesIds.length; row++){
			for(int col= 0; col<piecesIds[row].length; col++){
				_pieces[row][col]= new PieceSprite(gO.getPiecesImage(),
				gO.getPiecesImage().getWidth(null)/gO.getPieceTypesNumber(),
				gO.getPiecesImage().getHeight(null),
				gO.getDefaultPositions()[row][col]);
				wrapPieceToCell(row, col);
			}
		}
		
	}
	
	private void wrapPieceToCell(int inRow,int inCol){
		int pieceXCenter= _boardInUse.getCellX(inCol)+(_boardInUse.getCellBoardWidth()/2);
		int pieceYCenter= _boardInUse.getCellY(inRow)+(_boardInUse.getCellBoardHeight()/2);
		_pieces[inRow][inCol].setPosition(pieceXCenter, pieceYCenter);	
	}
	
	/**
	 * Set a piece at the given position. If there was a piece in this position
	 * it is overwritten.
	 * @param pieceId
	 * @param atRow
	 * @param atCol
	 * @return
	 */
	public void setPieceAt(PieceSprite piece,int atRow,int atCol){
		_pieces[atRow][atCol]= piece;
		wrapPieceToCell(atRow, atCol);
	}
	
	public boolean insertPieceIfOnTheBoard(PieceSprite piece){
		int pieceX= piece.getX()- _boardInUse.getX();
		int pieceY= piece.getY()- _boardInUse.getY();
		
		int col= (pieceX/_boardInUse.getCellBoardWidth());
		int row= (pieceY/_boardInUse.getCellBoardHeight()); 

		if((_pieces!=null)&&(row<_pieces.length)&&(col<_pieces[0].length)){
			_pieces[row][col]= piece;
			wrapPieceToCell(row, col);
			return true;
		}
		return false;
	}
	
	public PieceSprite removePieceAt(int atRow,int atCol){
		PieceSprite p= _pieces[atRow][atCol];
		_pieces[atRow][atCol]= null;		
		return p;
	}
	
	public PieceSprite removePieceAtPosition(int atX,int atY){
		for(int row= 0; row <_pieces.length ; row++){
			for(int col= 0; col <_pieces[row].length ; col++){
				
				if((_pieces[row][col]!=null)&&(_pieces[row][col].getCollisionRectangle().contains(atX, atY))){
					return removePieceAt(row,col);
				}
			}
		}
		return null;
	}
	
	public void paint(Graphics g){
		for(int row = 0; row < _pieces.length; row++){
			for(int col = 0; col < _pieces[row].length; col++){
				if(_pieces[row][col]!=null){
					_pieces[row][col].paint(g);
				}
			}
		}
	}
}
