//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Kalecser Kurtz, Fabio Roger Manera.

package wheel.reactive.sets;

import java.util.Collection;

import wheel.lang.Consumer;
import wheel.reactive.CollectionSignal;


public interface SetSignal<T> extends CollectionSignal<T> {

	void addSetReceiver(Consumer<SetValueChange<T>> receiver);
	void removeSetReceiver(Object receiver);

	public interface SetValueChange<E> {
		Collection<E> elementsAdded();
		Collection<E> elementsRemoved();
	}
	
	boolean currentContains(T element);
}
