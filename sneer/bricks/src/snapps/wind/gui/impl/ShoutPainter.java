package snapps.wind.gui.impl;

import javax.swing.JTextPane;

import snapps.wind.Shout;
import wheel.reactive.lists.ListSignal;

abstract class ShoutPainter {
	
	static void appendShout(Shout shout, JTextPane pane) {
		StringBuilder buffer = new StringBuilder();
		buffer.append(pane.getText());
		appendShout(shout,buffer);
		pane.setText(buffer.toString());
	}
	
	static void repaintAllShoults(ListSignal<Shout> listSignal, JTextPane pane) {
		StringBuilder buffer = new StringBuilder();
		for (Shout shout : listSignal) {
			appendShout(shout, buffer);
		}
		pane.setText(buffer.toString());
	}
	
	private static void appendShout(Shout shout, StringBuilder buffer) {
		if(ShoutUtils.isMyOwnShout(shout))
			buffer.append("Me");
		else
			buffer.append(ShoutUtils.publisherNick(shout));
		
		buffer.append(" - "). append(ShoutUtils.getFormatedShoutTime(shout)).append("\n");
		buffer.append(shout.phrase).append("\n\n");
	}
}
