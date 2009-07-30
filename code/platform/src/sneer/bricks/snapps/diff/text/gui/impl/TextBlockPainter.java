package sneer.bricks.snapps.diff.text.gui.impl;

import java.awt.Color;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import sneer.bricks.software.diff.TextComparator.TextBlock;
import sneer.bricks.software.diff.TextComparator.TextBlockType;
import sneer.foundation.lang.exceptions.NotImplementedYet;

class TextBlockPainter {
	
	private final DefaultStyledDocument _document;
	
	private Style _insert;
	private Style _delete;
	private Style _equal;
	
	LinkedTextBlock root;

	private final JCheckBox _onlyText;

	TextBlockPainter(DefaultStyledDocument styledDocument, JCheckBox onlyText) {
		_document = styledDocument;
		_onlyText = onlyText;
		Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		StyleConstants.setFontSize(def, 12);
		StyleConstants.setFontFamily(def, "Verdana");
		
		_equal = _document.addStyle("equal", def);
		_delete = _document.addStyle("equal", def);
		_insert = _document.addStyle("equal", def);
		
		StyleConstants.setForeground(_delete, Color.RED);
		StyleConstants.setStrikeThrough(_delete, true);
		StyleConstants.setForeground(_insert, new Color(35,160,35));
		
		_document.addStyle("equal", _equal);
		_document.addStyle("insert", _insert);
		_document.addStyle("delete", _delete);
	}
	
	void append(final List<TextBlock> blocks) {
		root = new LinkedTextBlock();		
		root._isRoot = true;
		LinkedTextBlock current = root;
		LinkedTextBlock previous = null;
		
		for (TextBlock block : blocks) {
			current._textBlock = block;

			try {
				final Style style;
				switch (block.type()) {
					case INSERT:  	style = _insert; break;
					case DELETE:  	style = _delete; break;
					default: 			style = _equal;
				}
				if(_onlyText.isSelected()) textAppend(block);
				else richAppend(block, style);

			} catch (BadLocationException e) {
				throw new NotImplementedYet(e); // Fix Handle this exception.
			}
			
			previous = current;
			current = new LinkedTextBlock();
			previous._next = current;
			current._previous = previous;
		}
		
		previous._next = root;
		root._previous = previous;
	}

	private void textAppend(TextBlock block) throws BadLocationException {
		if(block.type()==TextBlockType.DELETE){
			_document.insertString(_document.getLength(), "\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< MISSING\n",  _delete);
			_document.insertString(_document.getLength(), block.content() ,  _delete);
			_document.insertString(_document.getLength(), "\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< MISSING\n",  _delete);
			return;
		}
		
		if(block.type()==TextBlockType.INSERT){
			_document.insertString(_document.getLength(), "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> EXTRA\n",  _insert);
			_document.insertString(_document.getLength(), block.content() ,  _insert);
			_document.insertString(_document.getLength(), "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> EXTRA\n",  _insert);
			return;
		}
		
		_document.insertString(_document.getLength(), block.content() ,  _equal);
	}
	
	private void richAppend(TextBlock block, final Style style) throws BadLocationException {
		_document.insertString(_document.getLength(), block.content(),  style);
	}
	
	class LinkedTextBlock{
		
		boolean _isRoot;
		LinkedTextBlock _next;
		LinkedTextBlock _previous;
		
		TextBlock _textBlock;

		int[] positions(){
			int size = _textBlock.content().length();
			if(_isRoot) return new int[]{0, size};
			
			int offset = _previous.positions()[1];
			return new int[]{offset, offset+size};
		}
	}
}
