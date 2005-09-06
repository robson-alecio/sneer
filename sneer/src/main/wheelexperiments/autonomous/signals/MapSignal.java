//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Kalecser Kurtz, Fabio Roger Manera.

package wheelexperiments.autonomous.signals;

import java.util.Map;

public interface MapSignal<K, V> {

	void addObserver(Observer<K, V> observer);

	public interface Observer<Ko, Vo> {
		
		public void entryAdded(Ko newKey, Vo newValue);

		public void keyRemoved(Ko key, Vo value);
		
	}

	SetSignal keys();

	Map<K, V> sighting();  //TODO: Consider returning a MapSighting.


}
