package wheel.reactive.list;

import java.util.List;

import wheel.reactive.list.ListSignal.ListValueChange;
import wheel.reactive.list.ListSignal.ListValueChangeVisitor;

public final class ListReplaced implements ListValueChange {

	public void accept(ListValueChangeVisitor visitor) {
		visitor.listReplaced();
	}

}
