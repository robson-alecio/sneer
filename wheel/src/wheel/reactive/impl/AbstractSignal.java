//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Fabio Roger Manera.

package wheel.reactive.impl;


import java.util.HashSet;
import java.util.Set;

import wheel.lang.Omnivore;
import wheel.lang.Wrapper;
import wheel.reactive.Signal;
import wheel.reactive.sets.impl.SetValueChangeImpl;

public abstract class AbstractSignal<T> extends AbstractNotifier<T> implements Signal<T> {

	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		T currentValue = currentValue();
		if (currentValue == null) return "null";
		return currentValue.toString();
	}

	@Override
	public void addSetReceiver(Omnivore<SetValueChange<T>> receiver) {
		addReceiver(new SetReceiverAdapter<T>(receiver));
	}

	@Override
	public void removeSetReceiver(Object receiver) {
		removeReceiver(receiver);
	}

	@Override
	protected void initReceiver(Omnivore<? super T> receiver) {
		receiver.consume(currentValue());
	}
	
	@Override
	public Set<T> currentElements() {
		HashSet<T> result = new HashSet<T>(1);
		result.add(currentValue());
		return result;
	}

	static private class SetReceiverAdapter<AT> extends Wrapper<Omnivore<SetValueChange<AT>>> implements Omnivore<AT> {
		
		private AT _oldValue;
		
		public SetReceiverAdapter(Omnivore<SetValueChange<AT>> receiver) {
			super(receiver);
		}
		
		public void consume(AT newValue) {
			_delegate.consume(new SetValueChangeImpl<AT>(newValue, _oldValue));
			_oldValue = newValue;
		}
		
	}
	
}

