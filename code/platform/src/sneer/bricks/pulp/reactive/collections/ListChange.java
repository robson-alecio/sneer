package sneer.bricks.pulp.reactive.collections;

public interface ListChange<T> {
	
	void accept(Visitor<T> visitor);
	
	public interface Visitor<T> {
		void elementAdded(int index, T element);
		void elementRemoved(int index, T element);
		void elementReplaced(int index, T oldElement, T newElement);
		void elementMoved(int index, int newIndex, T newElement);
	}
}