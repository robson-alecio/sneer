package wheel.reactive.lists;

public interface ListValueChange<T> {
	
	void accept(Visitor<T> visitor);
	
	public interface Visitor<T> {
		void elementInserted(int index, T element);
		void elementAdded(int index, T element);
		void elementMoved(int oldIndex, int newIndex, T element);
		
		void elementRemoved(int index, T element);
		
		void elementReplaced(int index, T oldElement, T newElement);
	}
}