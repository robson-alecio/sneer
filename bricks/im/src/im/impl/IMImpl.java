package im.impl;

import im.IM;
import sneer.lego.Brick;
import console.Console;

public class IMImpl implements IM {

	@Brick
	private Console console;
	
	@Override
	public void sendMessage(String contactId, String message) {
		console.out("Sending message '"+message+"' to "+contactId);
	}

}
