package sneer.skin.swing.threadchecker.impl;

import javax.swing.SwingUtilities;

import sneer.skin.swing.threadchecker.ThreadChecker;
import wheel.io.Logger;

class ThreadCheckerImpl implements ThreadChecker{

	static Thread _swingThread;
	
	@Override
	public void check() {
		if(_swingThread==null)
			initialize();
		
		if(_swingThread!=Thread.currentThread())
			throw new RuntimeException("Swing is running in a another thread!");
	}

	private void initialize() {
		SwingUtilities.invokeLater(new Runnable(){ 
			Thread _threadToTest = Thread.currentThread();
			@Override public void run() {
				_swingThread = Thread.currentThread();
				Logger.log("Swing is running in a another thread!", _threadToTest, _swingThread);
			}
		});
	}
}
