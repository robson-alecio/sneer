package wheel.io.serialization;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.Register;
import wheel.reactive.impl.RegisterImpl;

public class DeepCopyingPipe {

	private final Omnivore<Object> _input = createInput();
	private final Register<Object> _output = new RegisterImpl<Object>(null);
	 
	public Omnivore<Object> input() {
		return _input;
	}

	private Omnivore<Object> createInput() {
		return new Omnivore<Object>() {	public void consume(Object valueObject) {
			Object copy = DeepCopier.deepCopy(valueObject);
			_output.setter().consume(copy);
		}};
	}

	public Signal<Object> output() {
		return _output.output();
	}

}
