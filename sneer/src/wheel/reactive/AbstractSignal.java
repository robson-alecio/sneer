//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Fabio Roger Manera.

package wheel.reactive;


import java.util.HashSet;
import java.util.Set;

import wheel.lang.Omnivore;

public abstract class AbstractSignal<T> extends AbstractNotifier<T> implements Signal<T> {

	@Override
	public String toString() {
		T currentValue = currentValue();
		if (currentValue == null)
			return "null"; // Oooo. Big discussion. What to return in case of null? Empty String? null? "null"? NullPointerException?
		return currentValue.toString();
	}

	public void addSetReceiver(Omnivore<SetValueChange<T>> receiver) {
		addReceiver(new SetReceiverAdapter<T>(receiver));
	}

	public void removeSetReceiver(Omnivore<SetValueChange<T>> receiver) {
		removeReceiver(new SetReceiverAdapter<T>(receiver));
	}

	public void addTransientSetReceiver(Omnivore<SetValueChange<T>> receiver) {
		addTransientReceiver(new SetReceiverAdapter<T>(receiver));
	}

	public void removeTransientSetReceiver(Omnivore<SetValueChange<T>> receiver) {
		removeTransientReceiver(new SetReceiverAdapter<T>(receiver));
	}
	
	@Override
	protected void initReceiver(Omnivore<T> receiver) {
		receiver.consume(currentValue());
	}
	
	public Set<T> currentElements() {
		HashSet<T> result = new HashSet<T>(1);
		result.add(currentValue());
		return result;
	}

	static private class SetReceiverAdapter<AT> implements Omnivore<AT> {
		
		private final Omnivore<SetValueChange<AT>> _delegate;
		private AT _oldValue;
		
		public SetReceiverAdapter(Omnivore<SetValueChange<AT>> receiver) {
			_delegate = receiver;
		}
		
		public void consume(AT newValue) {
			_delegate.consume(new SetValueChangeImpl<AT>(newValue, _oldValue));
			_oldValue = newValue;
		}

		@Override
		public boolean equals(Object other) {
			return _delegate.equals(other);
		}

		@Override
		public int hashCode() {
			return _delegate.hashCode();
		}
		
	}
	
}

