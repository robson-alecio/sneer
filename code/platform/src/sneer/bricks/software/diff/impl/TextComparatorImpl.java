package sneer.bricks.software.diff.impl;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import name.fraser.neil.plaintext.diff_match_patch;
import name.fraser.neil.plaintext.diff_match_patch.Diff;
import name.fraser.neil.plaintext.diff_match_patch.Operation;
import sneer.bricks.software.diff.TextComparator;
import sneer.foundation.lang.exceptions.NotImplementedYet;

class TextComparatorImpl implements TextComparator{
	
	@Override
	public List<TextBlock> diff(String text1, String text2){
		return diff(text1, text2, 32);
	}
	
	@Override
	public List<TextBlock> semanticCleanup(List<TextBlock> iterator){
		if(!(iterator instanceof TextBlockList)) throw new NotImplementedYet();
		
		LinkedList<Diff> diffs = ((TextBlockList) iterator)._diffs;
		new diff_match_patch().diff_cleanupSemantic(diffs);
		
		return new TextBlockList(diffs);
	}
	
	@Override
	public List<TextBlock> diff(String text1, String text2, int dualThreshold){
		diff_match_patch delegate = new diff_match_patch();
		delegate.Diff_DualThreshold = (short) dualThreshold;
		LinkedList<Diff> diffs = delegate.diff_main(text1, text2);
		return adaptIterator(diffs);
	}
	
	@Override
	public String toPrettyHtml(List<TextBlock> blocks){
	    StringBuilder html = new StringBuilder();
	    html.append("<html><body>");
	    
	    Iterator<TextBlock> blocksIterator = blocks.iterator();
	    
	    while(blocksIterator.hasNext()){
	    	TextBlock block = blocksIterator.next();
	    	String text = block.content().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\n", "&para;<BR>");
	    	
	    	switch (block.type()) {
	    		case INSERT:
	    			html.append("<font color='green'>").append(text).append("</font>");
	    			break;
	    		case DELETE:
	    			html.append("<font color='red'>").append(text).append("</font>");
	    			break;
	    		case EQUAL:
	    			html.append(text);
	    	}
	    }
	    html.append("</body></html>");
	    return html.toString();
	}
	
	private List<TextBlock> adaptIterator( LinkedList<name.fraser.neil.plaintext.diff_match_patch.Diff> diffs) {
		return new TextBlockList(diffs);
	}
	
	private class TextBlockList extends AbstractList<TextBlock>{
		private final LinkedList<name.fraser.neil.plaintext.diff_match_patch.Diff> _diffs;
		
		TextBlockList(LinkedList<name.fraser.neil.plaintext.diff_match_patch.Diff> diffs) {
			_diffs = diffs;
		}

		@Override public TextBlock get(int index) { return new TextBlockImpl(_diffs.get(index)); }
		@Override public int size() {return _diffs.size();}
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