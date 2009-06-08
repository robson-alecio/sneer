package spikes.lucass.game.type;

import java.awt.Image;

import javax.swing.ImageIcon;



public class GoOptions implements GameOptions{
	
	private static final int PIECE_TYPES_NUMBER= 1;
	
	private static final int EMPTY= -1;
	private static final int DOT=  0;
	
	private Image _boardImage= new ImageIcon( getClass().getResource("/spikes/lucass/res/boardGo74x74.png") ).getImage();
	private Image _piecesImage= new ImageIcon( getClass().getResource("/spikes/lucass/res/pecasGo74x74.png") ).getImage();
	
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
	
	public int[][] getDefaultPositions(){
		return _defaultPositions;
	}
	
	public int getRowNumber(){
		return _defaultPositions.length;
	}
	
	public int getColNumber(){
		return _defaultPositions[0].length;
	}

	public int getPieceTypesNumber() {
		return PIECE_TYPES_NUMBER;
	}

	public Image getBoardImage() {
		return _boardImage;
	}

	public Image getPiecesImage() {
		return _piecesImage;
	}

	public int getBoardCellVariation() {
		return 1;
	}

	public int getPieceIndex(String pieceName) {
		// TODO Auto-generated method stub
		return 0;
	}

	public String[] getPieceList() {
		// TODO Auto-generated method stub
		return new String[]{"LALALALA","LALALAL4","LALd","LAssssd"};
	}
}
