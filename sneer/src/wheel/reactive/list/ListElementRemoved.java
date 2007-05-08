package wheel.reactive.list;

import wheel.reactive.list.ListSignal.ListValueChange;
import wheel.reactive.list.ListSignal.ListValueChangeVisitor;

public class ListElementRemoved implements ListValueChange {

	private final int _index;

	public ListElementRemoved(int index) {
		_index = index;
	}

	public void accept(ListValueChangeVisitor visitor) {
		visitor.elementRemoved(_index);
	}

	@Override
	public String toString() {
		return "List element removed at " + _index;
	}
	
}
