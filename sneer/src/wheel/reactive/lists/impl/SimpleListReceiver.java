package wheel.reactive.lists.impl;

public abstract class SimpleListReceiver extends AbstractListReceiver {
	
	@Override
	public void elementReplaced(int index) {
		elementRemoved(index);
		elementAdded(index);
	}

	@Override
	public void listReplaced(int oldListSize, int newListSize) {
		int i = 0;
		while (i < oldListSize) elementRemoved(i++);
		i = 0;
		while (i < newListSize) elementAdded(i++);
	}

}
