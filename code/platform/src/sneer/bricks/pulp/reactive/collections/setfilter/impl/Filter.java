package sneer.bricks.pulp.reactive.collections.setfilter.impl;

import static sneer.foundation.environments.Environments.my;

import java.util.HashMap;
import java.util.Map;

import sneer.bricks.hardware.cpu.lang.contracts.WeakContract;
import sneer.bricks.hardware.ram.ref.weak.keeper.WeakReferenceKeeper;
import sneer.bricks.pulp.reactive.ReactivePredicate;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.collections.CollectionChange;
import sneer.bricks.pulp.reactive.collections.SetRegister;
import sneer.bricks.pulp.reactive.collections.SetSignal;
import sneer.bricks.pulp.reactive.collections.impl.SetRegisterImpl;
import sneer.foundation.lang.Consumer;

final class Filter<T> {

	private final SetSignal<T> _input;
	private final ReactivePredicate<T> _predicate;	
	private final SetRegister<T> _output;

	private final Map<T, WeakContract> _contractsByElement = new HashMap<T, WeakContract>();

	@SuppressWarnings("unused") private final WeakContract _contractOnInput;

	Filter(SetSignal<T> input, ReactivePredicate<T> predicate) {
		_input = input;
		_predicate = predicate;
		 _output = new SetRegisterImpl<T>();
		 
		 addElements(_input);
		 
		 _contractOnInput = _input.addReceiver(new Consumer<CollectionChange<T>>(){@Override public void consume(CollectionChange<T> change) {
			addElements(change.elementsAdded());
			remove(change.elementsRemoved());
		}});
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
		_contractsByElement.remove(element).dispose();
	}

	void add(final T element) {
		Signal<Boolean> signal = _predicate.evaluate(element);
		WeakContract contract = signal.addReceiver(new Consumer<Boolean>() { @Override public void consume(Boolean value) {
			if (value)
				_output.add(element);
			else
				_output.remove(element);
		}});
		
		_contractsByElement.put(element, contract);
	}
}