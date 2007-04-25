package wheel.reactive;

import wheel.reactive.ListSignal.ListValueChange;
import wheel.reactive.ListSignal.ListValueChangeVisitor;

public final class ListElementAdded<VO> implements ListValueChange<VO> {

	private final int _index;

	public ListElementAdded(int index) {
		_index = index;
	}

	@Override
	public void accept(ListValueChangeVisitor<VO> visitor) {
		visitor.elementAdded(_index);
	}
	
	@Override
	public String toString() {
		return "List element added at " + _index;
	}

}
