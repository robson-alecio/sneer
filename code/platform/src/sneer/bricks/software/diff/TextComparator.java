package sneer.bricks.software.diff;

import java.util.Iterator;

import sneer.foundation.brickness.Brick;

@Brick
public interface TextComparator {

	Iterator<TextBlock> diff(String text1, String text2);
	Iterator<TextBlock> diff(String text1, String text2, int dualThreshold);

	String toPrettyHtml(Iterator<TextBlock> blocksIterator);

	public interface TextBlock {
		TextBlockType type();
		String content();
	}
	
	public enum TextBlockType {
		INSERT,
		DELETE,
		EQUAL;
	}
}
