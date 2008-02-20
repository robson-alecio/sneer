package spikes.lego.store.impl;

import spikes.lego.store.ObjectStore;

public class ObjectStoreImpl implements ObjectStore {

	@Override
	public void store(Object obj) {
		System.out.println("Storing: "+obj);
	}

}
