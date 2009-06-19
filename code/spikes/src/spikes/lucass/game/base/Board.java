package spikes.lucass.game.base;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Board{
	
	private BufferedImage _board;
	private BufferedImage _boardSquares;
	
	private int _x;
	private int _y;
	private int _colNumber;
	private int _rowNumber;
	
	
	public Board(Image boardImage) {
		_boardSquares= new BufferedImage(boardImage.getWidth(null),boardImage.getHeight(null),BufferedImage.TYPE_INT_ARGB);
		_boardSquares.getGraphics().drawImage(boardImage, 0, 0, null);
	}

	public void generateBoard(int colNumber, int rowNumber,int boardCellVariation ) {
		_colNumber= colNumber;
		_rowNumber= rowNumber;
		
		int boardWidth=  (colNumber* (_boardSquares.getWidth()/boardCellVariation));
		int boardHeight= (rowNumber* _boardSquares.getHeight());
		
		int boardCellWidth=  (boardWidth / colNumber);
		int boardCellHeight= (boardHeight/rowNumber);
		
		_board= new BufferedImage(boardWidth, boardHeight,BufferedImage.TYPE_INT_ARGB);
		_board.getGraphics().clearRect(0, 0, _board.getWidth(), _board.getHeight());
		for(int i=0;i<colNumber;i++){
			for(int j=0;j<rowNumber;j++){
				int blackOrWhite=(i+j)% boardCellVariation;
				_board.getGraphics().drawImage(_boardSquares, i*boardCellWidth, j* boardCellHeight,(i*boardCellWidth)+boardCellWidth, (j*boardCellHeight)+boardCellHeight,
												blackOrWhite* boardCellWidth, 0, (blackOrWhite* boardCellWidth)+boardCellWidth, boardCellHeight , null);
			}
		}
	}
	
	public int getBoardHeight() {
		return (_rowNumber* _boardSquares.getHeight());
	}

	public int getBoardWidth() {
		return  (_colNumber*_boardSquares.getWidth())/2;
	}
	
	public int getCellBoardWidth() {
		return getBoardWidth()/_colNumber;
	}
	
	public int getCellBoardHeight() {
		return getBoardHeight()/_rowNumber;
	}

	public void setX(int x) {
		_x = x;
	}
	
	public int getX() {
		return _x;
	}

	public void setY(int y) {
		_y = y;
	}

	public int getY() {
		return _y;
	}

	public int getCellY(int atRow){
		return _y+(getCellBoardHeight()*atRow);
	}
	
	public int getCellX(int atCol){
		return _x+(getCellBoardWidth()*atCol);
	}
	
	public void paint(Graphics g){
		g.drawImage(_board,_x,_y , null);
	}
}
