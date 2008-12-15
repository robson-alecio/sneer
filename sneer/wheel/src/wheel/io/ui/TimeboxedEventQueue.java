package wheel.io.ui;

import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.Toolkit;

import wheel.io.Logger;
import wheel.lang.Environments;
import wheel.lang.Timebox;
import wheel.lang.Environments.Memento;

public class TimeboxedEventQueue extends EventQueue {

	private static Memento _environment;
	private static int _timeboxDuration;

	private static TimeboxedEventQueue _current;

	
	static public synchronized void startQueueing(int timeboxDuration) {
		if (_environment != null) throw new IllegalStateException("Already started.");
		_environment = Environments.memento();
		_timeboxDuration = timeboxDuration;
		
		startQueueing();
	}

	static public synchronized void stopQueueing() {
		_current.pop();
		_current = null;
		_environment = null;
	}

	private static void startQueueing() {
		_current = new TimeboxedEventQueue();
		
		Toolkit.getDefaultToolkit().getSystemEventQueue().push(_current);
	}

	static private synchronized void restart() {
		//_current.pop();  //Fix This is a leak. Call pop but find a way for the events in the queue not to be passed to the next (AWT) queue, maybe pushing a dummy queue before pushing the timeboxed queue.
		startQueueing();
	}


	
	@Override
	protected void dispatchEvent(final AWTEvent event) {
		new AWTEventTimebox(event);
	}

	private void superDispatchEvent(AWTEvent event) {
		super.dispatchEvent(event);
	}
	
	
	private class AWTEventTimebox extends Timebox {
		
		private AWTEventTimebox(AWTEvent event) {
			super(_timeboxDuration, false);
			_event = event;
			Environments.runWith(_environment, this);
		}

		
		private final AWTEvent _event;


		@Override 
		protected void runInTimebox() {
			superDispatchEvent(_event);
		}
		
		@Override
		protected void threadBlockedNotification(Thread thread) {
			super.threadBlockedNotification(thread);
			Logger.log("Starting new Gui Thread");
			restart();
		}
		
	}

}
