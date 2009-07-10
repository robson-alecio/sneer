package sneer.bricks.software.diff.impl;

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.lang.NotImplementedException;

import name.fraser.neil.plaintext.diff_match_patch;
import name.fraser.neil.plaintext.diff_match_patch.Operation;
import sneer.bricks.software.diff.Diff;
import sneer.bricks.software.diff.DiffType;
import sneer.bricks.software.diff.TextComparator;

class TextComparatorImpl implements TextComparator{
	
	private final diff_match_patch _delegate = new diff_match_patch();
	
	@Override
	public Iterator<Diff> diff(String text1, String text2){
		return adaptIterator(_delegate.diff_main(text1, text2));
	}
	
	@Override
	public String diffToHtml(Iterator<Diff> diffs){
		if(!(diffs instanceof DiffIterator)) throw new NotImplementedException();
		
		DiffIterator adapter = (DiffIterator) diffs;
		return _delegate.diff_prettyHtml(adapter._diffs);
	}
	
	@Override public void setDualThreshold(int dualThreshold) { _delegate.Diff_DualThreshold = (short)dualThreshold;}

	private Iterator<Diff> adaptIterator( LinkedList<name.fraser.neil.plaintext.diff_match_patch.Diff> diffs) {
		return new DiffIterator(diffs);
	}
	
	private class DiffIterator implements Iterator<Diff>{
		private final LinkedList<name.fraser.neil.plaintext.diff_match_patch.Diff> _diffs;
		private final Iterator<name.fraser.neil.plaintext.diff_match_patch.Diff> _iterator;
		
		public DiffIterator(LinkedList<name.fraser.neil.plaintext.diff_match_patch.Diff> diffs) {
			_diffs = diffs;
			_iterator = _diffs.iterator();
		}
		
		@Override public boolean hasNext() {return _iterator.hasNext(); }
		@Override public sneer.bricks.software.diff.Diff next() {return new DiffImpl(_iterator.next()); }
		@Override public void remove() { _iterator.remove(); }
	}
	
	private class DiffImpl implements Diff{
		
		private final name.fraser.neil.plaintext.diff_match_patch.Diff _dif;
		
		private DiffImpl(name.fraser.neil.plaintext.diff_match_patch.Diff dif){ _dif = dif; }

		@Override public String content() { return _dif._text; }
		@Override public DiffType type() {
			if(_dif._operation == Operation.DELETE) return DiffType.DELETE;
			if(_dif._operation == Operation.INSERT) return DiffType.INSERT;
			return DiffType.EQUAL;
		}
	}
}