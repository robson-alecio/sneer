package sneer.bricks.snapps.diff.text.gui.impl;

import java.awt.Color;
import java.util.Iterator;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import sneer.bricks.software.diff.TextComparator.TextBlock;
import sneer.foundation.lang.exceptions.NotImplementedYet;

class TextBlockPainter {
	
	private final DefaultStyledDocument _document;
	
	private Style _insert;
	private Style _delete;
	private Style _equal;

	TextBlockPainter(DefaultStyledDocument styledDocument) {
		_document = styledDocument;
		Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		StyleConstants.setFontSize(def, 11);
		
		_equal = _document.addStyle("equal", def);
		_delete = _document.addStyle("equal", def);
		_insert = _document.addStyle("equal", def);
		
		StyleConstants.setForeground(_delete, Color.BLACK);
		StyleConstants.setStrikeThrough(_delete, true);
		StyleConstants.setBackground(_delete, new Color(230,230,230));
		StyleConstants.setBackground(_insert, Color.GREEN);
		
		_document.addStyle("equal", _equal);
		_document.addStyle("insert", _insert);
		_document.addStyle("delete", _delete);
	}
	
	void append(Iterator<TextBlock> blocks) {
		while (blocks.hasNext()) 
			append(blocks.next());
	}
	
	void append(TextBlock block) {
		try {
			Style style;
	    	switch (block.type()) {
	    		case INSERT:
	    			style = _insert;
	    			break;
	    		case DELETE:
	    			style = _delete;
	    			break;
	    		default:
	    			style = _equal;
	    	}
    		_document.insertString(_document.getLength(), block.content(),  style);
		} catch (BadLocationException e) {
			throw new NotImplementedYet(e); // Fix Handle this exception.
		}	
	}
}
