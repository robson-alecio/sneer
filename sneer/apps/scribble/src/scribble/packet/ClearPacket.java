package scribble.packet;

import scribble.ScribblePacket;

public class ClearPacket implements ScribblePacket{
	
	public ClearPacket(){
	}

	public int type(){
		return CLEAR;
	}
	
}
