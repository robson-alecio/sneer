package wheel.reactive.impl;

import java.util.HashSet;
import java.util.Set;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public class ConstantSignal<T> implements Signal<T> {

	public ConstantSignal(T value) {
		_value = value;
	}
	
	private final T _value;

	@Override
	public void addReceiver(Omnivore<T> ignored) {}

	@Override
	public void addTransientReceiver(Omnivore<T> ignored) {}

	@Override
	public T currentValue() { return _value; }

	@Override
	public void removeReceiver(Omnivore<T> ignored) {}

	@Override
	public void removeTransientReceiver(Omnivore<T> ignored) {}

	@Override
	public void addSetReceiver(Omnivore<SetValueChange<T>> ignored) {}

	@Override
	public void addTransientSetReceiver(Omnivore<SetValueChange<T>> ignored) {}

	@Override
	public Set<T> currentElements() {
		HashSet<T> result = new HashSet<T>();
		result.add(currentValue());
		return result;
	}

	@Override
	public void removeSetReceiver(Omnivore<SetValueChange<T>> ignored) {}

	@Override
	public void removeTransientSetReceiver(Omnivore<SetValueChange<T>> ignored) {
	}

}
