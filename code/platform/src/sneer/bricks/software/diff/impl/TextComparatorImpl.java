package sneer.bricks.software.diff.impl;

import java.util.Iterator;
import java.util.LinkedList;

import name.fraser.neil.plaintext.diff_match_patch;
import name.fraser.neil.plaintext.diff_match_patch.Operation;
import sneer.bricks.software.diff.Diff;
import sneer.bricks.software.diff.DiffType;
import sneer.bricks.software.diff.TextComparator;

class TextComparatorImpl implements TextComparator{
	
	private final diff_match_patch _comparator = new diff_match_patch();
	
	@Override
	public Iterator<Diff> diffMain(String text1, String text2){
		LinkedList<name.fraser.neil.plaintext.diff_match_patch.Diff> diffs = _comparator.diff_main(text1, text2);
		final Iterator<name.fraser.neil.plaintext.diff_match_patch.Diff> iterator = diffs.iterator();
		
		return new Iterator<Diff>(){
			@Override public boolean hasNext() {return iterator.hasNext(); }
			@Override public sneer.bricks.software.diff.Diff next() {return new DiffImpl(iterator.next()); }
			@Override public void remove() { iterator.remove(); }
		};
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