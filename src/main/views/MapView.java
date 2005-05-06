//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Kalecser Kurtz, Fabio Roger Manera.

package views;

import java.util.Map;

public interface MapView<K, V> {

	void addObserver(Observer<K, V> observer);

	public interface Observer<Ko, Vo> {
		
		public void entryAdded(Ko newKey, Vo newValue);

		public void keyRemoved(Ko key);
		
	}

	SetView keys();

	Map<K, V> currentValue();


}
