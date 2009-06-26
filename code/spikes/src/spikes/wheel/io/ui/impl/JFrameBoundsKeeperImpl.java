package spikes.wheel.io.ui.impl;

import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import sneer.bricks.hardware.gui.guithread.GuiThread;
import spikes.wheel.io.ui.BoundsPersistence;
import spikes.wheel.io.ui.JFrameBoundsKeeper;
import static sneer.foundation.environments.Environments.my;

public class JFrameBoundsKeeperImpl implements JFrameBoundsKeeper {

	private final BoundsPersistence _persistence;
	
	public JFrameBoundsKeeperImpl(BoundsPersistence persistence) {
		_persistence = persistence;
	}
	
	public void keepBoundsFor(final JFrame frame, final String id){
		Runnable keepBoundsRunnable = keepBoundsRunnable(frame, id);
		
		if (SwingUtilities.isEventDispatchThread()){
			keepBoundsRunnable.run();
			return;
		}
		
		my(GuiThread.class).invokeAndWait(keepBoundsRunnable);
	}

	private Runnable keepBoundsRunnable(final JFrame frame, final String id) {
		return new Runnable() {
		
			@Override
			public void run() {
				restorePreviousBounds(id, frame);
				startKeepingBounds(id, frame);		
			}
		
		};
	}

	private void restorePreviousBounds(String id, JFrame frame) {
		Rectangle storedBounds = _persistence.getStoredBounds(id);
		if (storedBounds != null){
			frame.setBounds(storedBounds);
			frame.setPreferredSize(frame.getSize());
			frame.setVisible(true);
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
