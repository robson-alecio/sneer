package scribble.packet;


import java.awt.Color;

import scribble.ScribblePacket;


public class ColorPacket implements ScribblePacket{
	public final Color _color;
	
	public ColorPacket(Color color){
		_color = color;
	}

	public int type(){
		return COLOR;
	}
	
}
