package wheel.reactive.impl;

import java.util.Iterator;

import org.apache.commons.collections.iterators.EmptyIterator;
import org.apache.commons.collections.iterators.SingletonIterator;

import wheel.lang.Omnivore;
import wheel.lang.exceptions.NotImplementedYet;
import wheel.reactive.lists.ListValueChange;

public class Constant<TYPE> extends AbstractSignal<TYPE> {

	private final TYPE _constantValue;
	
	public Constant(TYPE constantValue){
		_constantValue = constantValue;
	}
	
	@Override
	public TYPE currentValue() {
		return _constantValue;
	}

	@Override
	public int currentSize() {
		return _constantValue == null ? 0 : 1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<TYPE> iterator() {
		if (_constantValue == null) return EmptyIterator.INSTANCE;
		return new SingletonIterator(_constantValue);
	}
	
	@Override
	public TYPE currentGet(int index) {
		if (index > 1) throw new IndexOutOfBoundsException();
		if (_constantValue == null) throw new IndexOutOfBoundsException();
		
		return _constantValue;
	}
	
	@Override
	public void removeListReceiver(Object receiver) {
		removeReceiver(receiver);
	}

	@Override
	public void addListReceiver(Omnivore<ListValueChange> receiver) {
		throw new NotImplementedYet(); //Implement
	}
}
