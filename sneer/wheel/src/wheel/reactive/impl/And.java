package wheel.reactive.impl;

import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.impl.RegisterImpl;

public class And {

	private final Register<Boolean> _result = new RegisterImpl<Boolean>(false);
	private final Signal<Boolean> _input1;
	private final Signal<Boolean> _input2;
	@SuppressWarnings("unused")
	private final EventReceiver<Boolean> _receiverToAvoidGc;
	
	public And(Signal<Boolean> input1, Signal<Boolean> input2) {
		_input1 = input1;
		_input2 = input2;
		_receiverToAvoidGc = new EventReceiver<Boolean>(input1, input2){@Override public void consume(Boolean newValueIgnored) {
			refresh();
		}};
	}
	
	private void refresh() {
		_result.setter().consume(_input1.currentValue() && _input2.currentValue());
	}

	public Signal<Boolean> output() {
		return new SignalOwnerReference<Boolean>(_result.output(), this);
	}
}