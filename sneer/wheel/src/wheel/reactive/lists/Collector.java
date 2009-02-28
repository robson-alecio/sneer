package wheel.reactive.lists;

import wheel.lang.Functor;
import wheel.reactive.lists.impl.ListRegisterImpl;
import wheel.reactive.lists.impl.VisitingListReceiver;

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
		public void elementInserted(int index, IN value) {
			throw new sneer.commons.lang.exceptions.NotImplementedYet(); // Implement
		}

		@Override
		public void elementMoved(int oldIndex, int newIndex, IN element) {
			throw new sneer.commons.lang.exceptions.NotImplementedYet(); // Implement
		}
	}

	private final ListRegister<OUT> _output = new ListRegisterImpl<OUT>();
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
