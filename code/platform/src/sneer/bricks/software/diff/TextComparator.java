package sneer.bricks.software.diff;

import java.util.Iterator;

import sneer.foundation.brickness.Brick;

@Brick
public interface TextComparator {

	Iterator<Diff> diffMain(String text1, String text2);

}
