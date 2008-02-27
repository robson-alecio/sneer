package wheel.reactive.lists.impl;

import wheel.reactive.lists.ListValueChange;


public final class ListElementToBeReplaced implements ListValueChange {

	private final int _index;

	public ListElementToBeReplaced(int index) {
		_index = index;
	}

	public void accept(Visitor visitor) {
		visitor.elementToBeReplaced(_index);
	}

}
