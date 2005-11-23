//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Kalecser Kurtz, Fabio Roger Manera.

package wheelexperiments.reactive;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import wheelexperiments.reactive.SetSignal.SetValueChange;


public class SetSource<T> extends AbstractNotifier<SetValueChange<T>>  implements SetSignal<T>, Serializable {

	private Set<T> _contents = new HashSet<T>();

	public void addSetReceiver(Receiver<SetValueChange<T>> receiver) {
		addReceiver(receiver);
	}

	public void removeSetReceiver(Receiver<SetValueChange<T>> receiver) {
		removeReceiver(receiver);
	}
	
	public void addTransientSetReceiver(Receiver<SetValueChange<T>> receiver) {
		addTransientReceiver(receiver);
	}

	public void removeTransientSetReceiver(Receiver<SetValueChange<T>> receiver) {
		removeTransientReceiver(receiver);
	}

	public Set<T> currentElements() {
		synchronized (_contents) {
			return contentsCopy();
		}
	}

	private Set<T> contentsCopy() {
		return new HashSet<T>(_contents);
	}

	public void add(T elementAdded) {
		change(new SetValueChangeImpl<T>(elementAdded, null));
	}

	public void remove(T elementRemoved) {
		change(new SetValueChangeImpl<T>(null, elementRemoved));
	}

	
	public void change(SetValueChange<T> change) {
		synchronized (_contents) {
			assertValidChange(change);
			_contents.addAll(change.elementsAdded());
			_contents.removeAll(change.elementsRemoved());
			notifyReceivers(change);
		}
	}

	private void assertValidChange(SetValueChange<T> change) {
		if (change.elementsAdded().removeAll(_contents))
			throw new IllegalArgumentException("SetSource already contained at least one element being added.");
		if (!_contents.containsAll(change.elementsRemoved()))
			throw new IllegalArgumentException("SetSource did not contain all elements being removed.");
	}

	@Override
	protected void initReceiver(Receiver<SetValueChange<T>> receiver) {
		receiver.receive(new SetValueChangeImpl<T>(contentsCopy(), null));
	}

	private static final long serialVersionUID = 1L;

}
