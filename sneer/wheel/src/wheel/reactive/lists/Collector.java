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
		public void elementAdded(int index) {
			_output.add(_functor.evaluate(_input.currentGet(index)));
		}

		@Override
		public void elementToBeRemoved(int ignored) {
			//The output source will do pre notification (to be removed) as well as notification (removed) when the element is removed from it.
		}

		@Override
		public void elementRemoved(int index) {
			_output.removeAt(index);
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
