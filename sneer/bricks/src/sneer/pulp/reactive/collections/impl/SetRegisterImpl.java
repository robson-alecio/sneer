//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Kalecser Kurtz, Fabio Roger Manera.

package sneer.pulp.reactive.collections.impl;

import static sneer.commons.environments.Environments.my;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.events.EventNotifier;
import sneer.pulp.events.EventNotifiers;
import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.Signals;
import sneer.pulp.reactive.collections.CollectionChange;
import sneer.pulp.reactive.collections.SetRegister;
import sneer.pulp.reactive.collections.SetSignal;



public class SetRegisterImpl<T> implements SetRegister<T> {


	private static final long serialVersionUID = 1L;

	private class MyOutput implements SetSignal<T> {

		private final EventNotifier<CollectionChange<T>> _notifier = my(EventNotifiers.class).create(new Consumer<Consumer<? super CollectionChange<T>>>(){@Override public void consume(Consumer<? super CollectionChange<T>> newReceiver) {
			if (_contents.isEmpty()) return;
			newReceiver.consume(new CollectionChangeImpl<T>(contentsCopy(), null));
		}});

		@Override
		public void addReceiver(Consumer<? super CollectionChange<T>> receiver) {
			_notifier.output().addReceiver(receiver);
		}

		@Override
		public void removeReceiver(Object receiver) {
			_notifier.output().removeReceiver(receiver);
		}

		@Override
		public Collection<T> currentElements() {
			synchronized (_contents) {
				return new HashSet<T>(_contents);
			}
		}

		@Override
		public int currentSize() {
			return size().currentValue();
		}

		@Override
		public Iterator<T> iterator() {
			return _contents.iterator();
		}

		private Set<T> contentsCopy() {
			return new HashSet<T>(_contents);
		}

		@Override
		public Signal<Integer> size() {
			return _size.output();
		}

		@Override
		public boolean currentContains(T element) {
			return _contents.contains(element);
		}
		
	}

	private final Set<T> _contents = new HashSet<T>();
	private final Register<Integer> _size = my(Signals.class).newRegister(0);

	private final MyOutput _output = new MyOutput();

	
	public SetSignal<T> output() {
		return _output;
	}

	public void add(T elementAdded) {
		change(new CollectionChangeImpl<T>(elementAdded, null));
	}

	public void remove(T elementRemoved) {
		change(new CollectionChangeImpl<T>(null, elementRemoved));
	}

	@Override
	public void change(CollectionChange<T> change) {
		synchronized (_contents) {
			preserveDeltas(change);
			if (change.elementsAdded().isEmpty() && change.elementsRemoved().isEmpty())
				return;
			
			_contents.addAll(change.elementsAdded());
			_contents.removeAll(change.elementsRemoved());
			_output._notifier.notifyReceivers(change);
			
			updateSize();
		}
	}

	private void updateSize() {
		Integer size = _contents.size();
		if (size != _size.output().currentValue())
			_size.setter().consume(size);
	}

	private void preserveDeltas(CollectionChange<T> change) {
		change.elementsAdded().removeAll(_contents);
		change.elementsRemoved().retainAll(_contents);
	}

}
