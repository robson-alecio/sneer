package sneer.bricks.pulp.internetaddresskeeper.impl;

import static sneer.foundation.environments.Environments.my;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import sneer.bricks.pulp.internetaddresskeeper.InternetAddress;
import sneer.bricks.software.bricks.statestore.BrickStateStore;
import sneer.bricks.software.bricks.statestore.impl.BrickStateStoreException;

abstract class Store {
	
	static List<Object[]> restore() {
		try {
			List<Object[]> addresses  = (List<Object[]>) my(BrickStateStore.class).readObjectFor(InternetAddress.class, InternetAddressKeeperImpl.class.getClassLoader());
			if (addresses != null)	
				return addresses;
		} catch (BrickStateStoreException ignore) {} 
		return new ArrayList<Object[]>();
	}
	
	static void save(Collection<InternetAddress> currentAddresses) {
		try {
			List<Object[]> addresses = new ArrayList<Object[]>();
			for (InternetAddress address : currentAddresses) 
				addresses.add(new Object[]{
					address.contact().nickname().currentValue(),  
					address.host(), 
					address.port()});

			my(BrickStateStore.class).writeObjectFor(InternetAddress.class, addresses);
		} catch (BrickStateStoreException ignore) {}
	 }
}