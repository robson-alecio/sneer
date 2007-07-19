package sneer.apps.draw.packet;

import sneer.apps.draw.DrawPacket;

public class BrushPacket implements DrawPacket{
	public final int _beginX;
	public final int _beginY;
	public final int _endX;
	public final int _endY;
	
	public BrushPacket(int beginX, int beginY, int endX, int endY){
		_beginX = beginX;
		_beginY = beginY;
		_endX = endX;
		_endY = endY;
	}

	public int type(){
		return BRUSH;
	}
	
}
