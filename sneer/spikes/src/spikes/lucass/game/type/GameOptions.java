package spikes.lucass.game.type;

import java.awt.Image;

public interface GameOptions {

	public int[][] getDefaultPositions();
	
	public int getRowNumber();
	
	public int getColNumber();
	
	public Image getBoardImage();
	
	public Image getPiecesImage();
	
	public int getPieceIndex(String pieceName);
	
	public int getPieceTypesNumber();
	
	public int getBoardCellVariation();
	
	public String[] getPieceList();
}
