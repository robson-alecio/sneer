//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Kalecser Kurtz, Fabio Roger Manera.

package wheelexperiments.reactive;

import java.util.Collection;
import java.util.Set;


public interface SetSignal<T>  {

	void addSetReceiver(Receiver<SetValueChange<T>> receiver);
	void removeSetReceiver(Receiver<SetValueChange<T>> receiver);

	void addTransientSetReceiver(Receiver<SetValueChange<T>> receiver);
	void removeTransientSetReceiver(Receiver<SetValueChange<T>> receiver);

	public interface SetValueChange<E> {
		Collection<E> elementsAdded();
		Collection<E> elementsRemoved();
	}

	Set<T> currentElements();
	
}
