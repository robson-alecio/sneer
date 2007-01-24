package spikes.lucass.GameBase;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class PieceSprite {
	
	private int _pieceIndex= -1;
	
	private int _x;
	private int _y;
	
	private Image _pieceSet;
	private boolean _isVisible= true;
	
	private int _pieceWidth;
	private int _pieceHeight;
	private int _pieceTransX;
	private int _pieceTransY;
	
	public PieceSprite(Image pieceSet,int pieceWidth, int pieceHeight,int index) {
		_pieceSet= pieceSet;
		_pieceWidth= pieceWidth;
		_pieceTransX= _pieceWidth/2;
		_pieceHeight= pieceHeight;
		_pieceTransY= _pieceHeight/2;
		setPieceIndex(index); 
	}

	public int getX() {
		return _x;
	}

	public void setX(int x) {
		_x = x;
	}

	public int getY() {
		return _y;
	}

	public void setY(int y) {
		_y = y;
	}
	
	public Rectangle getCollisionRectangle(){
		return new Rectangle(_x-_pieceTransX,_y-_pieceTransY,_pieceWidth,_pieceHeight);
	}
	
	public void setPosition(int x, int y){
		setX(x);
		setY(y);
	}

	private void setPieceIndex(int index) {
		_pieceIndex = index;
	}
	
	public int getPieceIndex(){
		return _pieceIndex;
	}

	public void paint(Graphics g) {
		if(_isVisible){
			int xCalc= _x-_pieceTransX;
			int yCalc= _y-_pieceTransY;
			g.drawImage(_pieceSet, xCalc, yCalc, xCalc+_pieceWidth, yCalc+_pieceHeight, 
					_pieceWidth*_pieceIndex, 0, (_pieceWidth*_pieceIndex)+_pieceWidth, _pieceHeight, null);
		}
	}

	public void setVisible(boolean b) {
		_isVisible= b;
	}
	
	public boolean getVisible() {
		return _isVisible;
	}
}
