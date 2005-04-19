//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Kalecser Kurtz.

package views;

import java.util.Map;

public interface MapView {

	void addObserver(Observer observer);

	public interface Observer {
		
		public void entryAdded(Object newKey, Object newValue);

		public void keyRemoved(Object key);
		
	}

	SetView keys();

	Map currentValue();


}
