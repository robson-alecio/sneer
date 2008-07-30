package wheel.reactive.impl;

import wheel.lang.Omnivore;
import wheel.reactive.Register;
import wheel.reactive.Signal;

public class Adder {

	private final Signal<Integer> _input1;
	private final Signal<Integer> _input2;
	private final Register<Integer> _sum = new RegisterImpl<Integer>(0);

	private final Omnivore<Integer> _receiver = createReceiver();

	
	public Adder(Signal<Integer> input1, Signal<Integer> input2) {
		_input1 = input1;
		_input2 = input2;
		input1.addReceiver(_receiver);
		input2.addReceiver(_receiver);
	}

	private Omnivore<Integer> createReceiver() {
		return new Omnivore<Integer>(){@Override public void consume(Integer newValueIgnored) {
			refresh();
		}};
	}

	private void refresh() {
		int sum = _input1.currentValue() + _input2.currentValue();
		_sum.setter().consume(sum);
	}

	public Signal<Integer> output() {
		return new SignalOwnerReference<Integer>(_sum.output(), this);
	}

}
