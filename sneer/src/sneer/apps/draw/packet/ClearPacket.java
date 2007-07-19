package sneer.apps.draw.packet;

import sneer.apps.draw.DrawPacket;

public class ClearPacket implements DrawPacket{
	
	public ClearPacket(){
	}

	public int type(){
		return CLEAR;
	}
	
}
