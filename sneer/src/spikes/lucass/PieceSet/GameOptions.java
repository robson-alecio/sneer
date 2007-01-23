package spikes.lucass.PieceSet;

import java.awt.Image;

public abstract class GameOptions {
	//TODO: n�o pode ser uma interface
	public abstract int[][] getDefaultPositions();
	
	public abstract int getRowNumber();
	
	public abstract int getColNumber();
	
	public abstract Image getBoardImage();
	
	public abstract Image getPiecesImage();
	
	public abstract int getPieceIndex(int pieceID);
	
	public abstract int getPieceTypesNumber();
	
	public abstract int getBoardCellVariation();
}
