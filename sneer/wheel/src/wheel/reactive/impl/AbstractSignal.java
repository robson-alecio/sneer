//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Fabio Roger Manera.

package wheel.reactive.impl;

import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public abstract class AbstractSignal<T> extends AbstractNotifier<T> implements Signal<T> {

	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		T currentValue = currentValue();
		if (currentValue == null) return "null";
		return currentValue.toString();
	}

	@Override
	protected void initReceiver(Omnivore<? super T> receiver) {
		receiver.consume(currentValue());
	}
}