package wheel.reactive;

import java.util.List;

import wheel.reactive.ListSignal.ListValueChangeVisitor;

public abstract class ListValueChangeVisitorAdapter<T> implements
		ListValueChangeVisitor<T> {

	@Override
	public void elementAdded(int index) {}

	@Override
	public void elementRemoved(int index, T element) {}

	@Override
	public void elementReplaced(int index, T oldElement) {}

	@Override
	public void listReplaced(List<T> newList) {}


}
