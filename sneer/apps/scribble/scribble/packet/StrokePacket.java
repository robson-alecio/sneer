package scribble.packet;

import scribble.ScribblePacket;

public class StrokePacket implements ScribblePacket{
	public final int _size;
	
	public StrokePacket(int size){
		_size = size;
	}

	public int type(){
		return STROKE;
	}
	
}
