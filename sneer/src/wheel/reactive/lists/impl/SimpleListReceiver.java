package wheel.reactive.lists.impl;

public abstract class SimpleListReceiver extends AbstractListReceiver {
	
	@Override
	public void elementReplaced(int index) {
		elementRemoved(index);
		elementAdded(index);
	}

	@Override
	public void elementToBeRemoved(int index) {		
	}

	@Override
	public void elementRemoved(int index) {		
	}

}
