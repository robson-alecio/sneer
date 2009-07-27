package sneer.bricks.software.diff;

import java.util.List;

import sneer.foundation.brickness.Brick;

@Brick
public interface TextComparator {

	List<TextBlock> diff(String text1, String text2);
	List<TextBlock> diff(String text1, String text2, int dualThreshold);

	List<TextBlock> semanticCleanup(List<TextBlock> iterator);
	String toPrettyHtml(List<TextBlock> blocksIterator);

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
