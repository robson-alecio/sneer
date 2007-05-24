package wheel.reactive.lists;

import java.util.List;

import wheel.reactive.lists.ListSignal.ListValueChangeVisitor;

public abstract class ListValueChangeVisitorAdapter implements ListValueChangeVisitor {

	public void elementAdded(int index) {}

	public void elementRemoved(int index) {}

	public void elementReplaced(int index) {}

	public void listReplaced() {}


}
