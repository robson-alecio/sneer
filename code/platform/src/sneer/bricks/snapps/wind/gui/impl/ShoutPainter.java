package sneer.bricks.snapps.wind.gui.impl;

import java.awt.Color;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import sneer.bricks.pulp.reactive.collections.ListSignal;
import sneer.bricks.snapps.wind.Shout;
import sneer.foundation.lang.exceptions.NotImplementedYet;

class ShoutPainter {
	
	private final DefaultStyledDocument _document;
	
	private Style _space;
	private Style _time;
	private Style _shout;
	private Style _nick;

	ShoutPainter(DefaultStyledDocument styledDocument) {
		_document = styledDocument;
		Style def = StyleContext.getDefaultStyleContext().
        getStyle(StyleContext.DEFAULT_STYLE);

		_space = _document.addStyle("space", def);
		StyleConstants.setFontSize(_space, 4);

		_time = _document.addStyle("time", def);
		StyleConstants.setFontFamily(_time, "Verdana");
		StyleConstants.setFontSize(_time, 9);
		StyleConstants.setForeground(_time, Color.LIGHT_GRAY);

		_nick = _document.addStyle("nick", _time);
		StyleConstants.setForeground(_nick, Color.BLACK);
		StyleConstants.setFontSize(_nick, 10);
		StyleConstants.setBold(_nick, true);

		_shout = _document.addStyle("shout", def);
		StyleConstants.setFontFamily(_shout, "Verdana");
		StyleConstants.setFontSize(_shout, 12);		
		
		_document.addStyle("time", _time);
		_document.addStyle("shout", _shout);
	}

	void repaintAllShoults(ListSignal<Shout> listSignal) {
		try {
			_document.remove(0, _document.getLength());
		} catch (BadLocationException e) {
			throw new NotImplementedYet(e); // Fix Handle this exception.
		}
		for (Shout shout : listSignal) 
			appendShout(shout);
	}
	
	void appendShout(Shout shout) {
		try {
			_document.insertString(_document.getLength(), nick(shout) ,  _nick);
			_document.insertString(_document.getLength(), header(shout) ,  _time);
			_document.insertString(_document.getLength(), shout.phrase ,  _shout);
			_document.insertString(_document.getLength(), "\n\n" ,  _space);
		} catch (BadLocationException e) {
			throw new NotImplementedYet(e); // Fix Handle this exception.
		}	
	}
	
	private String header(Shout shout){		
		return new StringBuilder().append(" - ")
			.append(ShoutUtils.getFormatedShoutTime(shout)).append("\n").toString();
	}

	private String nick(Shout shout) {
		if(ShoutUtils.isMyOwnShout(shout)) return "|W|";
		
		return ShoutUtils.publisherNick(shout);
	}
}
