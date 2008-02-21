package spikes.legobricks.store.impl;

import spikes.lego.Brick;
import spikes.legobricks.security.Sentinel;
import spikes.legobricks.store.ObjectStore;

public class ObjectStoreImpl implements ObjectStore {

	private static final String RESOURCE_NAME = "Object Store";
	
	@Brick
	private Sentinel sentinel;
	
	@Override
	public void store(Object obj) {
		sentinel.check(RESOURCE_NAME);
		System.out.println("Storing: "+obj);
	}

}
