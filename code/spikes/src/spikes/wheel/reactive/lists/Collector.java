package spikes.wheel.reactive.lists;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.pulp.reactive.collections.CollectionSignals;
import sneer.bricks.pulp.reactive.collections.ListRegister;
import sneer.bricks.pulp.reactive.collections.ListSignal;
import sneer.bricks.pulp.reactive.collections.impl.VisitingListReceiver;
import sneer.foundation.lang.Functor;

public class Collector<IN, OUT> {

	public class MyReceiver extends VisitingListReceiver<IN> {

		public MyReceiver(ListSignal<IN> input) {
			super(input);
		}

		@Override
		public void elementAdded(int index, IN value) {
			_output.add(_functor.evaluate(value));
		}

		@Override
		public void elementRemoved(int index, IN value) {
			_output.removeAt(index);
		}

		@Override
		public void elementReplaced(int index, IN oldValue, IN newValue) {
			_output.replace(index, _functor.evaluate(_input.currentGet(index)));
		}

		@Override
		public void elementMoved(int index, int newIndex, IN newElement) {
			throw new sneer.foundation.lang.exceptions.NotImplementedYet(); // Implement
		}
	}

	private final ListRegister<OUT> _output = my(CollectionSignals.class).newListRegister();
	private final Functor<IN, OUT> _functor;

	public Collector(ListSignal<IN> input, Functor<IN, OUT> functor) {
		_functor = functor;
		
		for (IN element : input)
			_output.add(_functor.evaluate(element));
		new MyReceiver(input);
	}

	public ListSignal<OUT> output() {
		return _output.output();
	}

}
