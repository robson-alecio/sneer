package wheel.io.ui.impl;

import java.awt.Rectangle;

public interface BoundsPersistence {

	Rectangle getStoredBounds(String id);
	void storeBounds(String id, Rectangle bounds);

}
