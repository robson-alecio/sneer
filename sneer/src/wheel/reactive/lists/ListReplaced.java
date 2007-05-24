package wheel.reactive.lists;

import java.util.List;

import wheel.reactive.lists.ListSignal.ListValueChange;
import wheel.reactive.lists.ListSignal.ListValueChangeVisitor;

public final class ListReplaced implements ListValueChange {
	
	static public final ListReplaced SINGLETON = new ListReplaced();

	private ListReplaced(){}
	
	public void accept(ListValueChangeVisitor visitor) {
		visitor.listReplaced();
	}

	@Override
	public String toString() {
		return "List replaced";
	}
}
