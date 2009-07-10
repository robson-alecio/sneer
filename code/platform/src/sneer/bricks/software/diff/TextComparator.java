package sneer.bricks.software.diff;

import java.util.Iterator;

public interface TextComparator {

	Iterator<Diff> diff(String text1, String text2);
	String diffToHtml(Iterator<Diff> diffs);

	void setDualThreshold(int dualThreshold);
}
