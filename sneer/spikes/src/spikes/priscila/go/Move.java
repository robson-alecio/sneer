package spikes.priscila.go;

import spikes.priscila.go.GoBoard.StoneColor;

public class Move {
	
	private boolean _isResign;
	private boolean _isPass;
	private int _xCoordinate;
	private int _YCoordinate;
	
	public Move(boolean resign, boolean pass, int x, int y) {
		setResign(resign);
		setPass(pass);
		setXCoordinate(x);
		setYCoordinate(y);
	}
	
	public boolean isResign() {
		return _isResign;
	}

	public void setResign(boolean isResign) {
		_isResign = isResign;
	}

	public boolean isPass() {
		return _isPass;
	}

	public void setPass(boolean isPass) {
		_isPass = isPass;
	}

	public int getXCoordinate() {
		return _xCoordinate;
	}

	public void setXCoordinate(int coordinate) {
		_xCoordinate = coordinate;
	}

	public int getYCoordinate() {
		return _YCoordinate;
	}

	public void setYCoordinate(int coordinate) {
		_YCoordinate = coordinate;
	}

}
