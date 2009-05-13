package sneer.pulp.reactive.collections.setfilter.impl;

import static sneer.commons.environments.Environments.my;

import java.util.HashMap;
import java.util.Map;

import sneer.hardware.cpu.lang.Consumer;
import sneer.hardware.cpu.lang.ref.weakreferencekeeper.WeakReferenceKeeper;
import sneer.pulp.reactive.ReactivePredicate;
import sneer.pulp.reactive.Signals;
import sneer.pulp.reactive.collections.CollectionChange;
import sneer.pulp.reactive.collections.SetRegister;
import sneer.pulp.reactive.collections.SetSignal;
import sneer.pulp.reactive.collections.impl.SetRegisterImpl;

final class Filter<T> {

	private final SetSignal<T> _input;
	private final ReactivePredicate<T> _predicate;	
	private final SetRegister<T> _output;

	private Map<T, Consumer<?>> _receiversByElement = new HashMap<T, Consumer<?>>();

	Filter(SetSignal<T> input, ReactivePredicate<T> predicate) {
		_input = input;
		_predicate = predicate;
		 _output = new SetRegisterImpl<T>();
		 
		 add(_input);
		 
		 Consumer<CollectionChange<T>> receiver = new Consumer<CollectionChange<T>>(){@Override public void consume(CollectionChange<T> change) {
			add(change.elementsAdded());
			remove(change.elementsRemoved());
		}};
		my(Signals.class).receive(this, receiver, _input);
	}

	SetSignal<T> output() {
		return my(WeakReferenceKeeper.class).keep(_output.output(), this);
	}

	private void add(Iterable<T> elements) {
		for (T element : elements)
			add(element);
	}

	private void remove(Iterable<T> elements) {
		for (T element : elements)
			remove(element);
	}

	void remove(T element) {
		_output.remove(element);
		Consumer<?> receiver = _receiversByElement.remove(element);
		_predicate.evaluate(element).removeReceiver(receiver);
	}

	void add(final T element) {
		Consumer<Boolean> receiver = new Consumer<Boolean>() { @Override public void consume(Boolean value) {
			if (value)
				_output.add(element);
			else
				_output.remove(element);
		}};
		
		my(Signals.class).receive(this, receiver,_predicate.evaluate(element));
		_receiversByElement.put(element, receiver);
	}
}