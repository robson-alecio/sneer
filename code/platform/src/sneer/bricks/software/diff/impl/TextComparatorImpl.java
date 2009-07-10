package sneer.bricks.software.diff.impl;

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.lang.NotImplementedException;

import name.fraser.neil.plaintext.diff_match_patch;
import name.fraser.neil.plaintext.diff_match_patch.Operation;
import sneer.bricks.software.diff.TextComparator;

class TextComparatorImpl implements TextComparator{
	
	@Override
	public Iterator<TextBlock> diff(String text1, String text2){
		return diff(text1, text2, 32);
	}
	
	@Override
	public Iterator<TextBlock> diff(String text1, String text2, int dualThreshold){
		diff_match_patch delegate = new diff_match_patch();
		delegate.Diff_DualThreshold = (short) dualThreshold;
		return adaptIterator(delegate.diff_main(text1, text2));
	}
	
	@Override
	public String toPrettyHtml(Iterator<TextBlock> blocksIterator){
		if(!(blocksIterator instanceof TextBlockImpl)) throw new NotImplementedException();
		
		TextBlockIterator adapter = (TextBlockIterator) blocksIterator;
		return new diff_match_patch().diff_prettyHtml(adapter._diffs);
	}
	
	private Iterator<TextBlock> adaptIterator( LinkedList<name.fraser.neil.plaintext.diff_match_patch.Diff> diffs) {
		return new TextBlockIterator(diffs);
	}
	
	private class TextBlockIterator implements Iterator<TextBlock>{
		private final LinkedList<name.fraser.neil.plaintext.diff_match_patch.Diff> _diffs;
		private final Iterator<name.fraser.neil.plaintext.diff_match_patch.Diff> _iterator;
		
		public TextBlockIterator(LinkedList<name.fraser.neil.plaintext.diff_match_patch.Diff> diffs) {
			_diffs = diffs;
			_iterator = _diffs.iterator();
		}
		
		@Override public boolean hasNext() {return _iterator.hasNext(); }
		@Override public TextBlock next() {return new TextBlockImpl(_iterator.next()); }
		@Override public void remove() { _iterator.remove(); }
	}
	
	private class TextBlockImpl implements TextBlock{
		
		private final name.fraser.neil.plaintext.diff_match_patch.Diff _dif;
		
		private TextBlockImpl(name.fraser.neil.plaintext.diff_match_patch.Diff dif){ _dif = dif; }

		@Override public String content() { return _dif._text; }
		@Override public TextBlockType type() {
			if(_dif._operation == Operation.DELETE) return TextBlockType.DELETE;
			if(_dif._operation == Operation.INSERT) return TextBlockType.INSERT;
			return TextBlockType.EQUAL;
		}
	}
}