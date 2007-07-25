package wheel.io.ui.impl;

import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JFrame;

import org.prevayler.foundation.Cool;

import wheel.io.Log;
import wheel.io.Streams;
import wheel.io.files.Directory;
import wheel.io.files.impl.tranzient.TransientDirectory;
import wheel.io.ui.JFrameBoundsKeeper;
import wheel.lang.Threads;
import wheel.lang.exceptions.NotImplementedYet;

public class JFrameBoundsKeeperImpl implements JFrameBoundsKeeper {

	
	private final BoundsPersistence _persistence;
	
	public JFrameBoundsKeeperImpl(	final BoundsPersistence persistence) {
		_persistence = persistence;
	}
	
	/* (non-Javadoc)
	 * @see wheel.io.ui.impl.JFrameBoundsKeeper#keepBoundsFor(javax.swing.JFrame, java.lang.String)
	 */
	public void keepBoundsFor(final JFrame frame, final String id){
		restorePreviousBounds(id, frame);
		startKeepingBounds(id, frame);
	}

	private void restorePreviousBounds(String id, JFrame frame) {
		Rectangle storedBounds = _persistence.getStoredBounds(id);
		if (storedBounds != null){
			frame.setBounds(storedBounds);
		}
	}

	private void startKeepingBounds(final String id, final JFrame frame) {
		frame.addComponentListener(new ComponentAdapter() {
			
			@Override
			public void componentResized(ComponentEvent e) {
				boundsChanged();
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
				boundsChanged();
			}	
			
			private void boundsChanged() {
				_persistence.storeBounds(id, frame.getBounds());
			}	
		
		});
	}
	
	
	

}
