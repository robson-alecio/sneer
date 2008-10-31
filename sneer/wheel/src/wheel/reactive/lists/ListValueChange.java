package wheel.reactive.lists;

public interface ListValueChange {
	
	void accept(Visitor visitor);
	
	public interface Visitor {
		void elementAdded(int index, Object value);
		void elementToBeRemoved(int index);
		void elementRemoved(int index);
		void elementToBeReplaced(int index);
		void elementReplaced(int index);
	}
}