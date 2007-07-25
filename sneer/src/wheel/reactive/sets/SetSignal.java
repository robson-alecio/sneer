//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Kalecser Kurtz, Fabio Roger Manera.

package wheel.reactive.sets;

import java.util.Collection;
import java.util.Set;

import wheel.lang.Omnivore;


public interface SetSignal<T>  {

	void addSetReceiver(Omnivore<SetValueChange<T>> receiver);
	void removeSetReceiver(Omnivore<SetValueChange<T>> receiver);

	void addTransientSetReceiver(Omnivore<SetValueChange<T>> receiver);
	void removeTransientSetReceiver(Omnivore<SetValueChange<T>> receiver);

	public interface SetValueChange<E> {
		Collection<E> elementsAdded();
		Collection<E> elementsRemoved();
	}

	Set<T> currentElements();
	
}
