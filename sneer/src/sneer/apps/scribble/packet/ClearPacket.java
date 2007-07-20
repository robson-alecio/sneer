package sneer.apps.scribble.packet;

import sneer.apps.scribble.ScribblePacket;

public class ClearPacket implements ScribblePacket{
	
	public ClearPacket(){
	}

	public int type(){
		return CLEAR;
	}
	
}
