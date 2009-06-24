package sneer.bricks.pulp.internetaddresskeeper.impl;

import static sneer.foundation.environments.Environments.my;

import java.util.ArrayList;
import java.util.List;

import sneer.bricks.hardware.io.brickstatestore.BrickStateStore;
import sneer.bricks.pulp.blinkinglights.BlinkingLights;
import sneer.bricks.pulp.blinkinglights.LightType;
import sneer.bricks.pulp.internetaddresskeeper.InternetAddress;
import sneer.bricks.pulp.internetaddresskeeper.InternetAddressKeeper;

class Store{
	
	private final int TIMEOUT = 30*1000;
	private List<Object[]> _addresses;
	
	Store(){
		try {
			_addresses  = (List<Object[]>) my(BrickStateStore.class).readObjectFor(InternetAddress.class, getClass().getClassLoader());
		} catch (Throwable e) {
			initializeOnError(e);
		} 
	 }

	private void initializeOnError(Throwable e) {
		BlinkingLights bl = my(BlinkingLights.class);
		bl.  turnOn(LightType.WARN, "Unable to restore Contacts", "Sneer can't restore your contacts, using hardcoded Contacts", e, TIMEOUT);
		_addresses = new ArrayList<Object[]>();
	}

	 void save() {
		try {
			List<Object[]> addresses = new ArrayList<Object[]>();
			for (InternetAddress address : my(InternetAddressKeeper.class).addresses()) 
				addresses.add(new Object[]{
					address.contact().nickname().currentValue(),  
					address.host(), 
					address.port()});

			my(BrickStateStore.class).writeObjectFor(InternetAddress.class, addresses);
			_addresses = addresses;
		} catch (Exception e) {
			BlinkingLights bl = my(BlinkingLights.class);
			bl.turnOn(LightType.ERROR, "Unable to store Contacts", null, e, TIMEOUT);
		}
	 }

	List<Object[]> getRestoredAddresses() {
		return _addresses;
	}
}