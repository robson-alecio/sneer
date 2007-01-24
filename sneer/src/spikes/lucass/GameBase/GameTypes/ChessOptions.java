package spikes.lucass.GameBase.GameTypes;

import java.awt.Image;

import javax.swing.ImageIcon;



public class ChessOptions extends GameOptions{
	
	private static final int PIECE_TYPES_NUMBER= 12;
	
	private static final int EMPTY= -1;
	
	//IDS
	public static final int BLACK_TOWER=  0 ;
	public static final int BLACK_HORSE=  1 ;
	public static final int BLACK_BISHOP= 2 ;
	public static final int BLACK_QUEEN=  3 ;
	public static final int BLACK_KING=   4 ;
	public static final int BLACK_PAWN=   5 ;
	
	public static final int WHITE_TOWER=  6 ;
	public static final int WHITE_HORSE=  7 ;
	public static final int WHITE_BISHOP= 8 ;
	public static final int WHITE_QUEEN=  9 ;
	public static final int WHITE_KING=   10;
	public static final int WHITE_PAWN=   11;
	
	private static final int B_T_INDEX=  0;//tower
	private static final int B_H_INDEX=  1;//horse
	private static final int B_B_INDEX= 2;//bishop
	private static final int B_Q_INDEX=  3;//queen
	private static final int B_K_INDEX=   4;//king
	private static final int B_P_INDEX=   5;//pawn
	
	private static final int W_T_INDEX=  6;
	private static final int W_H_INDEX=  7;
	private static final int W_B_INDEX= 8;
	private static final int W_Q_INDEX=  9;
	private static final int W_K_INDEX=   10;
	private static final int W_P_INDEX=   11;
	
	private Image _boardImage= new ImageIcon( getClass().getResource("/spikes/lucass/res/board74x74.png") ).getImage();
	private Image _piecesImage= new ImageIcon( getClass().getResource("/spikes/lucass/res/pecas74x74.png") ).getImage();
	
	private int[][] _defaultPositions= {
			{B_T_INDEX,B_H_INDEX,B_B_INDEX,B_Q_INDEX,B_K_INDEX,B_B_INDEX,B_H_INDEX,B_T_INDEX,B_T_INDEX},
			{B_P_INDEX,B_P_INDEX,B_P_INDEX,B_P_INDEX,B_P_INDEX,B_P_INDEX,B_P_INDEX,B_P_INDEX,B_T_INDEX},
			{EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    },
			{EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    },
			{EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    },
			{EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    },
			{W_P_INDEX,W_P_INDEX,W_P_INDEX,W_P_INDEX,W_P_INDEX,W_P_INDEX,W_P_INDEX,W_P_INDEX,B_T_INDEX},
			{W_T_INDEX,W_H_INDEX,W_B_INDEX,W_Q_INDEX,W_K_INDEX,W_B_INDEX,W_H_INDEX,W_T_INDEX,B_T_INDEX},
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
		return 2;
	}

	@Override
	public int getPieceIndex(int pieceID) {
		// TODO Auto-generated method stub
		return 0;
	}
}
