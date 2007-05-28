package wheel.reactive.lists.impl;

import java.util.List;

import wheel.reactive.lists.ListValueChange;
import wheel.reactive.lists.ListValueChange.Visitor;


public final class ListReplaced implements ListValueChange {
	
	private final int _oldListSize;
	private final int _newListSize;

	public ListReplaced(int oldListSize, int newListSize) {
		_oldListSize = oldListSize;
		_newListSize = newListSize;
	}
	
	public void accept(Visitor visitor) {
		visitor.listReplaced(_oldListSize, _newListSize);
	}

}
