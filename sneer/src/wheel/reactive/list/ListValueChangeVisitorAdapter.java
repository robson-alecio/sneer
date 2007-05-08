package wheel.reactive.list;

import java.util.List;

import wheel.reactive.list.ListSignal.ListValueChangeVisitor;

public abstract class ListValueChangeVisitorAdapter<T> implements
		ListValueChangeVisitor<T> {

	public void elementAdded(int index) {}

	public void elementRemoved(int index, T element) {}

	public void elementReplaced(int index, T oldElement) {}

	public void listReplaced(List<T> newList) {}


}
