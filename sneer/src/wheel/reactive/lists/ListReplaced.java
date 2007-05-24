package wheel.reactive.lists;

import java.util.List;


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
