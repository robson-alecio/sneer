package scribble.packet;

import scribble.ScribblePacket;

public class StrokePacket implements ScribblePacket {

	private static final long serialVersionUID = 1L;
	public final int _size;
	
	public StrokePacket(int size){
		_size = size;
	}

	public int type(){
		return STROKE;
	}
	
}
