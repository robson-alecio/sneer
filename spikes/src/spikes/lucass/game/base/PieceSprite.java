package spikes.lucass.game.base;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public class PieceSprite {
	
	private int _pieceIndex= -1;
	
	private int _x;
	private int _y;
	
	private Image _pieceSet;
	private boolean _isVisible= true;
	
	private int _pieceWidth;
	private int _pieceHeight;
	private int _pieceTranslationX;
	private int _pieceTranslationY;
	
	public PieceSprite(Image pieceSet,int pieceWidth, int pieceHeight,int index) {
		_pieceSet= pieceSet;
		_pieceWidth= pieceWidth;
		_pieceTranslationX= _pieceWidth/2;
		_pieceHeight= pieceHeight;
		_pieceTranslationY= _pieceHeight/2;
		setPieceIndex(index); 
	}

	public void setVisible(boolean b) {
		_isVisible= b;
	}

	public boolean getVisible() {
		return _isVisible;
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

	public void setPosition(int x, int y){
		setX(x);
		setY(y);
	}

	public Rectangle getCollisionRectangle(){
		return new Rectangle(_x-_pieceTranslationX,_y-_pieceTranslationY,_pieceWidth,_pieceHeight);
	}
	
	private void setPieceIndex(int index) {
		_pieceIndex = index;
	}
	
	public int getPieceIndex(){
		return _pieceIndex;
	}

	public void paint(Graphics g) {
		if(_isVisible && _pieceIndex>=0){
			int xCalc= _x-_pieceTranslationX;
			int yCalc= _y-_pieceTranslationY;
			g.drawImage(_pieceSet, xCalc, yCalc, xCalc+_pieceWidth, yCalc+_pieceHeight, 
					_pieceWidth*_pieceIndex, 0, (_pieceWidth*_pieceIndex)+_pieceWidth, _pieceHeight, null);
		}
	}
}
