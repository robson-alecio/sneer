package sneer.bricks.software.diff.impl;

import sneer.bricks.software.diff.TextComparator;
import sneer.bricks.software.diff.TextComparators;

class TextComparatorsImpl  implements TextComparators{

	@Override
	public TextComparator newComparator() {
		return new TextComparatorImpl();
	}
	
}
