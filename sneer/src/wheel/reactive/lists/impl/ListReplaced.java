package wheel.reactive.lists.impl;

import java.util.List;

import wheel.reactive.lists.ListValueChange;
import wheel.reactive.lists.ListValueChange.Visitor;


public final class ListReplaced implements ListValueChange {
	
	static public final ListReplaced SINGLETON = new ListReplaced();

	private ListReplaced(){}
	
	public void accept(Visitor visitor) {
		visitor.listReplaced();
	}

	@Override
	public String toString() {
		return "List replaced";
	}
}
