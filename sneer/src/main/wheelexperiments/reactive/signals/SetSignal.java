//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Kalecser Kurtz, Fabio Roger Manera.

package wheelexperiments.reactive.signals;

import java.util.Set;


public interface SetSignal<T>  {

	public interface Receiver<TO> {
		public void elementAdded(TO newElement);
		public void elementRemoved(TO removedElement);
	}
	
	void addReceiver(Receiver<T> receiver);
	void removeReceiver(Receiver<T> receiver);

	Set<T> currentValue();

}
