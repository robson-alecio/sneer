package spikes.lucass.game.type;

import java.awt.Image;
import java.util.Vector;

import javax.swing.ImageIcon;


public class ChessOptions implements GameOptions{
	
	private static final int PIECE_TYPES_NUMBER= 12;
	
	private static final int EMPTY= -1;
	
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
	
	private static Vector<PieceNameWithPieceIndex> _piecesNames;
	
	static{
		_piecesNames= new Vector<PieceNameWithPieceIndex>();
		_piecesNames.add(new PieceNameWithPieceIndex(B_T_INDEX,"Torre Preta"));
		_piecesNames.add(new PieceNameWithPieceIndex(B_H_INDEX,"Cavalo Preto"));
		_piecesNames.add(new PieceNameWithPieceIndex(B_B_INDEX,"Bispo Preto"));
		_piecesNames.add(new PieceNameWithPieceIndex(B_Q_INDEX,"Rainha Preta"));
		_piecesNames.add(new PieceNameWithPieceIndex(B_K_INDEX,"Rei Preto"));
		_piecesNames.add(new PieceNameWithPieceIndex(B_P_INDEX,"Peão Preto"));
		_piecesNames.add(new PieceNameWithPieceIndex(W_T_INDEX,"Torre Branca"));
		_piecesNames.add(new PieceNameWithPieceIndex(W_H_INDEX,"Cavalo Branco"));
		_piecesNames.add(new PieceNameWithPieceIndex(W_B_INDEX,"Bispo Branco"));
		_piecesNames.add(new PieceNameWithPieceIndex(W_Q_INDEX,"Rainha Branca"));
		_piecesNames.add(new PieceNameWithPieceIndex(W_K_INDEX,"Rei Branco"));
		_piecesNames.add(new PieceNameWithPieceIndex(W_P_INDEX,"Peão Branco"));
	}
	
	private Image _boardImage= new ImageIcon( getClass().getResource("/spikes/lucass/res/board74x74.png") ).getImage();
	private Image _piecesImage= new ImageIcon( getClass().getResource("/spikes/lucass/res/pecas74x74.png") ).getImage();
	
	private int[][] _defaultPositions= {
			{B_T_INDEX,B_H_INDEX,B_B_INDEX,B_Q_INDEX,B_K_INDEX,B_B_INDEX,B_H_INDEX,B_T_INDEX},
			{B_P_INDEX,B_P_INDEX,B_P_INDEX,B_P_INDEX,B_P_INDEX,B_P_INDEX,B_P_INDEX,B_P_INDEX},
			{EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    },
			{EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    },
			{EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    },
			{EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    ,EMPTY    },
			{W_P_INDEX,W_P_INDEX,W_P_INDEX,W_P_INDEX,W_P_INDEX,W_P_INDEX,W_P_INDEX,W_P_INDEX},
			{W_T_INDEX,W_H_INDEX,W_B_INDEX,W_Q_INDEX,W_K_INDEX,W_B_INDEX,W_H_INDEX,W_T_INDEX},
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
		return 2;
	}

	public int getPieceIndex(String pieceName) {
		for(int i=0; i<_piecesNames.size(); i++){
			if(_piecesNames.elementAt(i).toString().equals(pieceName)){
				System.out.println(_piecesNames.elementAt(i).pieceIndex());
				return _piecesNames.elementAt(i).pieceIndex();
			}
		}
		return -1;
	}

	public String[] getPieceList() {
		String[] nameList= new String[_piecesNames.size()];
		for(int i=0; i<_piecesNames.size(); i++){
			nameList[i]= _piecesNames.elementAt(i).toString();
		}
		
		return nameList;
	}
}

class PieceNameWithPieceIndex{
	private int _pieceIndex;
	private String _pieceName;
	
	public PieceNameWithPieceIndex(int pieceIndex, String pieceName){
		setPieceIndex(pieceIndex);
		setPieceName(pieceName);
	}	
	
	public int pieceIndex() {
		return _pieceIndex;
	}
	public void setPieceIndex(int pieceIndex) {
		_pieceIndex = pieceIndex;
	}
	
	@Override
	public String toString() {
		return pieceName();
	}
	
	public String pieceName() {
		return _pieceName;
	}
	public void setPieceName(String pieceName) {
		_pieceName = pieceName;
	}	
}
