package spikes.sandro.listsort;

import wheel.reactive.Signal;
import wheel.reactive.impl.mocks.RandomBoolean;

class SortTestElement {

	static private int _counter;

	private final RandomBoolean _isOnline = new RandomBoolean();
	private final String _nick = "Nick" + _counter++;

	public Signal<Boolean> isOnline() {
		return _isOnline.output();
	}

	public String nick() {
		return _nick;
	}

}
