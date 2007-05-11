package wheel.reactive.list;

import wheel.reactive.list.ListSignal.ListValueChange;
import wheel.reactive.list.ListSignal.ListValueChangeVisitor;

public final class ListElementReplaced implements ListValueChange {

	private final int _index;

	public ListElementReplaced(int index) {
		_index = index;
	}

	public void accept(ListValueChangeVisitor visitor) {
		visitor.elementReplaced(_index);
	}

	@Override
	public String toString() {
		return "List element replaced at " + _index;
	}
	
}
