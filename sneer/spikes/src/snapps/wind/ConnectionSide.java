package snapps.wind;

import wheel.lang.Omnivore;

public interface ConnectionSide {

	void registerReceiver(Omnivore<Object> receiver);
	Omnivore<Object> sender();
	
}
