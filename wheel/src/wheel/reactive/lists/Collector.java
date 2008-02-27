package wheel.reactive.lists;

import wheel.lang.Functor;
import wheel.reactive.lists.impl.ListSourceImpl;
import wheel.reactive.lists.impl.VisitingListReceiver;

public class Collector<IN, OUT> {


	public class MyReceiver extends VisitingListReceiver {

		@Override
		public void elementAdded(int index) {
			_output.add(_functor.evaluate(_input.currentGet(index)));
		}

		@Override
		public void elementToBeRemoved(int ignored) {
			//The output source will do pre notification (to be removed) as well as notification (removed) when the element is removed from it.
		}

		@Override
		public void elementRemoved(int index) {
			_output.remove(index);
		}

		@Override
		public void elementToBeReplaced(int ignored) {
			//The output source will do pre notification (to be replaced) as well as notification (replaced) when the element is replaced there.
		}

		@Override
		public void elementReplaced(int index) {
			_output.replace(index, _functor.evaluate(_input.currentGet(index)));
		}

	}

	private final ListSignal<IN> _input;
	private final ListSource<OUT> _output = new ListSourceImpl<OUT>();
	private final Functor<IN, OUT> _functor;

	public Collector(ListSignal<IN> input, Functor<IN, OUT> functor) {
		_input = input;
		_functor = functor;
		
		for (IN element : _input)
			_output.add(_functor.evaluate(element));
		_input.addListReceiver(new MyReceiver());
	}

	public ListSignal<OUT> output() {
		return _output.output();
	}

}
