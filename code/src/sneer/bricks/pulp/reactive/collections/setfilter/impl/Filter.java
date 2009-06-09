package sneer.bricks.pulp.reactive.collections.setfilter.impl;

import static sneer.foundation.commons.environments.Environments.my;

import java.util.HashMap;
import java.util.Map;

import sneer.bricks.hardware.cpu.lang.Consumer;
import sneer.bricks.hardware.cpu.lang.ref.weakreferencekeeper.WeakReferenceKeeper;
import sneer.bricks.pulp.reactive.ReactivePredicate;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.pulp.reactive.collections.CollectionChange;
import sneer.bricks.pulp.reactive.collections.SetRegister;
import sneer.bricks.pulp.reactive.collections.SetSignal;
import sneer.bricks.pulp.reactive.collections.impl.SetRegisterImpl;

final class Filter<T> {

	private final SetSignal<T> _input;
	private final ReactivePredicate<T> _predicate;	
	private final SetRegister<T> _output;

	private final Map<T, Consumer<?>> _receiversByElement = new HashMap<T, Consumer<?>>();
	private final Map<T, Signal<Boolean>> _signalsByElement = new HashMap<T, Signal<Boolean>>();

	@SuppressWarnings("unused") private final Object _referenceToAvoidGc;

	Filter(SetSignal<T> input, ReactivePredicate<T> predicate) {
		_input = input;
		_predicate = predicate;
		 _output = new SetRegisterImpl<T>();
		 
		 addElements(_input);
		 
		 Consumer<CollectionChange<T>> receiver = new Consumer<CollectionChange<T>>(){@Override public void consume(CollectionChange<T> change) {
			addElements(change.elementsAdded());
			remove(change.elementsRemoved());
		}};

		_referenceToAvoidGc = my(Signals.class).receive(_input, receiver);
	}

	SetSignal<T> output() {
		return my(WeakReferenceKeeper.class).keep(_output.output(), this);
	}

	private void addElements(Iterable<T> elements) {
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
		_signalsByElement.remove(element);
	}

	void add(final T element) {
		Consumer<Boolean> receiver = new Consumer<Boolean>() { @Override public void consume(Boolean value) {
			if (value)
				_output.add(element);
			else
				_output.remove(element);
		}};
		
		Signal<Boolean> signal = _predicate.evaluate(element);
		signal.addReceiver(receiver);
		
		_signalsByElement.put(element, signal);
		_receiversByElement.put(element, receiver);
	}
}