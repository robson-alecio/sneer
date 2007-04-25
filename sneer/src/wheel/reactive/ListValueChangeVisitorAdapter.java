package wheel.reactive;

import java.util.List;

import wheel.reactive.ListSignal.ListValueChangeVisitor;

public abstract class ListValueChangeVisitorAdapter<T> implements
		ListValueChangeVisitor<T> {

	public void elementAdded(int index) {}

	public void elementRemoved(int index, T element) {}

	public void elementReplaced(int index, T oldElement) {}

	public void listReplaced(List<T> newList) {}


}
