package spikes.lucass.PieceSet;

import java.awt.Image;

import javax.swing.ImageIcon;


public class GoOptions extends GameOptions{
	
	private static final int PIECE_TYPES_NUMBER= 1;
	
	private static final int EMPTY= -1;
	private static final int DOT=  0;
	
	private Image _boardImage= new ImageIcon( getClass().getResource("/res/boardGo74x74.png") ).getImage();
	private Image _piecesImage= new ImageIcon( getClass().getResource("/res/pecasGo74x74.png") ).getImage();
	
	private int[][] _defaultPositions= {
			{DOT,EMPTY,DOT,EMPTY,DOT},
			{EMPTY,DOT,EMPTY,DOT,EMPTY},
			{DOT,EMPTY,DOT,EMPTY,DOT},
			{EMPTY,DOT,EMPTY,DOT,EMPTY},
			{DOT,EMPTY,DOT,EMPTY,DOT},
			{EMPTY,DOT,EMPTY,DOT,EMPTY},
			{DOT,EMPTY,DOT,EMPTY,DOT},
			{EMPTY,DOT,EMPTY,DOT,EMPTY},
	};
	
	@Override
	public int[][] getDefaultPositions(){
		return _defaultPositions;
	}
	
	@Override
	public int getRowNumber(){
		return _defaultPositions.length;
	}
	
	@Override
	public int getColNumber(){
		return _defaultPositions[0].length;
	}

	@Override
	public int getPieceTypesNumber() {
		return PIECE_TYPES_NUMBER;
	}

	@Override
	public Image getBoardImage() {
		return _boardImage;
	}

	@Override
	public Image getPiecesImage() {
		return _piecesImage;
	}

	@Override
	public int getBoardCellVariation() {
		return 1;
	}

	@Override
	public int getPieceIndex(int pieceID) {
		// TODO Auto-generated method stub
		return 0;
	}
}
