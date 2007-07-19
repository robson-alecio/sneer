package sneer.apps.draw.packet;

import sneer.apps.draw.DrawPacket;

public class StrokePacket implements DrawPacket{
	public final int _size;
	
	public StrokePacket(int size){
		_size = size;
	}

	public int type(){
		return STROKE;
	}
	
}
