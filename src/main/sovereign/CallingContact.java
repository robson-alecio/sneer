//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Fabio Roger Manera.

package sovereign;

public class CallingContact extends ThreadLocal<LifeView> {

	public synchronized LifeView life() {
		return get();
	}

	public synchronized void life(LifeView life) {
		set(life);
	}
	
}
