package sneer.skin.widgets.reactive.impl;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;

public class ClipboardOwnerImpl implements ClipboardOwner {

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
		throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
	}

}
