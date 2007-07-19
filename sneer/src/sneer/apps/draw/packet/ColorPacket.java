package sneer.apps.draw.packet;

import java.awt.Color;

import sneer.apps.draw.DrawPacket;

public class ColorPacket implements DrawPacket{
	public final Color _color;
	
	public ColorPacket(Color color){
		_color = color;
	}

	public int type(){
		return COLOR;
	}
	
}
