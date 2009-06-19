package spikes.sandro.listsort;

import sneer.bricks.pulp.reactive.Signal;
import spikes.wheel.reactive.impl.mocks.RandomBoolean;

class SortTestElement {

	static private int _counter;

	private final RandomBoolean _isOnline = new RandomBoolean();
	private final String _nick; {
		_counter++;
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < _counter; i++) 
			builder.append("*");
		
		_nick = builder.toString();
	} ;

	public Signal<Boolean> isOnline() {
		return _isOnline.output();
	}

	public String nick() {
		return _nick;
	}

}
