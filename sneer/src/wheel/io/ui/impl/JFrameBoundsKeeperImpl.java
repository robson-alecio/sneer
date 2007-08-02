package wheel.io.ui.impl;

import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import wheel.io.Log;
import wheel.io.ui.BoundsPersistence;
import wheel.io.ui.JFrameBoundsKeeper;

public class JFrameBoundsKeeperImpl implements JFrameBoundsKeeper {

	private final BoundsPersistence _persistence;
	
	public JFrameBoundsKeeperImpl(BoundsPersistence persistence) {
		_persistence = persistence;
	}
	
	public void keepBoundsFor(final JFrame frame, final String id){
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
			
				@Override
				public void run() {
					restorePreviousBounds(id, frame);
					startKeepingBounds(id, frame);		
				}
			
			});
		} catch (InterruptedException e) {
			Log.log(e);
		} catch (InvocationTargetException e) {
			Log.log(e);
		}
	}

	private void restorePreviousBounds(String id, JFrame frame) {
		Rectangle storedBounds = _persistence.getStoredBounds(id);
		if (storedBounds != null){
			frame.setBounds(storedBounds);
			frame.setPreferredSize(frame.getSize());
		}
	}

	private void startKeepingBounds(final String id, final JFrame frame) {
		
		frame.addComponentListener(new ComponentAdapter() {
		
			@Override
			public void componentResized(ComponentEvent e) {
				boundsChanged();
			}

			private void boundsChanged() {
				
				if (!frame.isVisible())
					return;
				
				Rectangle bounds = frame.getBounds();
				if (_persistence.getStoredBounds(id) == bounds)
					return;
				
				_persistence.setBounds(id, bounds);
				_persistence.store();
			}
		
			@Override
			public void componentMoved(ComponentEvent e) {
				boundsChanged();
			}
		
		});
	}
	
	
	

}
