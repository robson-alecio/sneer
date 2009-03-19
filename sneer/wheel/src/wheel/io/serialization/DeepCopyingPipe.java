package wheel.io.serialization;

import sneer.pulp.reactive.Signal;
import wheel.lang.Consumer;
import wheel.reactive.Register;
import wheel.reactive.impl.RegisterImpl;

public class DeepCopyingPipe {

	private final Consumer<Object> _input = createInput();
	private final Register<Object> _output = new RegisterImpl<Object>(null);
	 
	public Consumer<Object> input() {
		return _input;
	}

	private Consumer<Object> createInput() {
		return new Consumer<Object>() {	public void consume(Object valueObject) {
			Object copy = DeepCopier.deepCopy(valueObject);
			_output.setter().consume(copy);
		}};
	}

	public Signal<Object> output() {
		return _output.output();
	}

}
