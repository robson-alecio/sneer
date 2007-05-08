package wheel.reactive.list;

import wheel.reactive.list.ListSignal.ListValueChange;
import wheel.reactive.list.ListSignal.ListValueChangeVisitor;

public final class ListElementAdded implements ListValueChange {

	private final int _index;

	public ListElementAdded(int index) {
		_index = index;
	}

	public void accept(ListValueChangeVisitor visitor) {
		visitor.elementAdded(_index);
	}

}
