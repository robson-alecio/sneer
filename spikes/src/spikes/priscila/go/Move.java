package spikes.priscila.go;


public class Move {
	
	public final boolean isResign;
	public final boolean isPass;
	public final int xCoordinate;
	public final int yCoordinate;
	
	public Move(boolean resign_, boolean pass_, int x_, int y_) {
		isResign = resign_;
		isPass = pass_;
		xCoordinate = x_;
		yCoordinate = y_;
	}

}
