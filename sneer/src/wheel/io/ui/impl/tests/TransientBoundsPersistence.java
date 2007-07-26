package wheel.io.ui.impl.tests;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import wheel.io.ui.impl.BoundsPersistence;

public class TransientBoundsPersistence implements BoundsPersistence {

	private Map<String, Rectangle> _bounds = new HashMap<String, Rectangle>();

	@Override
	public synchronized Rectangle getStoredBounds(String id) {
		return _bounds .get(id);
	}

	@Override
	public synchronized void storeBounds(String id, Rectangle bounds) {
		_bounds.put(id, bounds);
	}

}
