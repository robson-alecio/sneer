package spikes.wheel.io.ui.impl.tests;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import spikes.wheel.io.ui.BoundsPersistence;


public class TransientBoundsPersistence implements BoundsPersistence {

	private Map<String, Rectangle> _bounds = new HashMap<String, Rectangle>();
	private Map<String, Rectangle> _pendingBounds = new HashMap<String, Rectangle>();
	
	@Override
	public synchronized Rectangle getStoredBounds(String id) {
		return _bounds.get(id);
	}

	@Override
	public synchronized void setBounds(String id, Rectangle bounds) {
		_pendingBounds.put(id, bounds);
	}

	@Override
	public synchronized void store() {
		_bounds.putAll(_pendingBounds);
	}


}
