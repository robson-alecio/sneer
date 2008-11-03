package wheel.reactive.lists;

public interface ListValueChange<T> {
	
	void accept(Visitor<T> visitor);
	
	public interface Visitor<T> {
		void elementInserted(int index, T value);
		void elementAdded(int index, T value);
		
		void elementToBeRemoved(int index, T value);
		void elementRemoved(int index, T value);
		
		void elementToBeReplaced(int index, T oldValue, T newValue);
		void elementReplaced(int index, T oldValue, T newValue);
	}
}