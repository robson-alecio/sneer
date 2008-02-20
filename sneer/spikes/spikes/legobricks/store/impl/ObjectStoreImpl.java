package spikes.legobricks.store.impl;

import spikes.legobricks.store.ObjectStore;

public class ObjectStoreImpl implements ObjectStore {

	@Override
	public void store(Object obj) {
		System.out.println("Storing: "+obj);
	}

}
