package wheel.io.serialization;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.Source;
import wheel.reactive.impl.SourceImpl;

public class DeepCopyingPipe {

	private final Omnivore<Object> _input = createInput();
	private final Source<Object> _output = new SourceImpl<Object>(null);
	 
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
