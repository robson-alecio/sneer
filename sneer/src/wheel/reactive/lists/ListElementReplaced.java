package wheel.reactive.lists;


public final class ListElementReplaced implements ListValueChange {

	private final int _index;

	public ListElementReplaced(int index) {
		_index = index;
	}

	public void accept(Visitor visitor) {
		visitor.elementReplaced(_index);
	}

	@Override
	public String toString() {
		return "List element replaced at " + _index;
	}
	
}
