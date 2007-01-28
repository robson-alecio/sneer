package spikes.lucass.GameBase.GameTypes;

import java.awt.Image;

public interface GameOptions {

	public int[][] getDefaultPositions();
	
	public int getRowNumber();
	
	public int getColNumber();
	
	public Image getBoardImage();
	
	public Image getPiecesImage();
	
	public int getPieceIndex(int pieceID);
	
	public int getPieceTypesNumber();
	
	public int getBoardCellVariation();
}
