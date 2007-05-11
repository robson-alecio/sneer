package wheel.reactive.list;

import java.util.List;

import wheel.reactive.list.ListSignal.ListValueChange;
import wheel.reactive.list.ListSignal.ListValueChangeVisitor;

public final class ListReplaced implements ListValueChange {
	
	static public final ListReplaced SINGLETON = new ListReplaced();

	private ListReplaced(){}
	
	public void accept(ListValueChangeVisitor visitor) {
		visitor.listReplaced();
	}

}
