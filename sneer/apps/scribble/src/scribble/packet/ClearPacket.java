package scribble.packet;

import scribble.ScribblePacket;

public class ClearPacket implements ScribblePacket{
	
	private static final long serialVersionUID = 1L;

	public ClearPacket(){
	}

	public int type(){
		return CLEAR;
	}
	
}
