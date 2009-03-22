package wheel.reactive.impl;

import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.impl.RegisterImpl;

public class Adder {

	private final Register<Integer> _sum = new RegisterImpl<Integer>(0);
	private final Signal<Integer> _input1;
	private final Signal<Integer> _input2;
	@SuppressWarnings("unused")
	private final EventReceiver<Integer> _referenceToAvoidGc;
	
	public Adder(Signal<Integer> input1, Signal<Integer> input2) {
		_input1 = input1;
		_input2 = input2;
		_referenceToAvoidGc = new EventReceiver<Integer>(input1, input2){@Override public void consume(Integer newValueIgnored) {
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
