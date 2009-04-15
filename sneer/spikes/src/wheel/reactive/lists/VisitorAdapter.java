package wheel.reactive.lists;

import sneer.pulp.reactive.collections.ListChange.Visitor;

public abstract class VisitorAdapter<T> implements Visitor<T> {

	@Override public void elementAdded(int index, T element) { /* Ignore */};
	@Override public void elementInserted(int index, T element) { /* Ignore */};
	@Override public void elementRemoved(int index, T element) { /* Ignore */};
	@Override public void elementReplaced(int index, T oldElement, T newElement) { /* Ignore */};
	@Override public void elementMoved(int oldIndex, int newIndex, T element) { /* Ignore */ };
}