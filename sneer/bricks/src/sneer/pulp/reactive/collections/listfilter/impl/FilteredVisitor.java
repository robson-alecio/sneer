package sneer.pulp.reactive.collections.listfilter.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.reactive.ReactivePredicate;
import sneer.pulp.reactive.collections.SetChange;
import sneer.pulp.reactive.collections.SetRegister;
import sneer.pulp.reactive.collections.SetSignal;
import sneer.pulp.reactive.collections.impl.SetRegisterImpl;
import wheel.reactive.impl.SetSignalOwnerReference;

final class FilteredVisitor<T> {

	private final SetSignal<T> _input;
	private final ReactivePredicate<T> _predicate;	
	private final SetRegister<T> _output;

	private Map<T, Consumer<?>> _receiversByElement = new HashMap<T, Consumer<?>>();

	FilteredVisitor(SetSignal<T> input, ReactivePredicate<T> predicate) {
		_input = input;
		_predicate = predicate;
		 _output = new SetRegisterImpl<T>();
		 
		 add(_input);
		
		_input.addReceiver(_receiverAvoidGc);
	}

	SetSignal<T> output() {
		return new SetSignalOwnerReference<T>(_output.output(), this);
	}
	
	private final Consumer<SetChange<T>> _receiverAvoidGc = new Consumer<SetChange<T>>(){@Override public void consume(SetChange<T> change) {
		add(change.elementsAdded());
		remove(change.elementsRemoved());
	}};

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
		
		_predicate.evaluate(element).addReceiver(receiver);
		_receiversByElement.put(element, receiver);
	}
}