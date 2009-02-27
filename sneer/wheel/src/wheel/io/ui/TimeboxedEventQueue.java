package wheel.io.ui;

import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import sneer.brickness.Environments;
import sneer.brickness.Environments.Memento;

import wheel.io.Logger;
import wheel.lang.Timebox;

public class TimeboxedEventQueue extends EventQueue {

	private static Memento _environment;
	private static int _timeboxDuration;

	private static List<TimeboxedEventQueue> _queues = new ArrayList<TimeboxedEventQueue>();

	
	static public synchronized void startQueueing(int timeboxDuration) {
		if (!_queues.isEmpty()) throw new IllegalStateException("Queueing already started.");

		_environment = Environments.memento();
		_timeboxDuration = timeboxDuration;
		
		startNewQueue();
	}

	static public synchronized void stopQueueing() {
		for (TimeboxedEventQueue queue : _queues) queue.pop();
		_queues.clear();
		_environment = null;
	}

	private static void startNewQueue() {
		TimeboxedEventQueue newQueue = new TimeboxedEventQueue();
		_queues.add(0, newQueue);
		Toolkit.getDefaultToolkit().getSystemEventQueue().push(newQueue);
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

			startNewQueue(); //This is an EventQueue leak. Not serious compared to the thread leak of the blocked threads that make these new EventQueues necessary. If these EventQueues are ever discarded (popped), take care because pending events will be automatically passed on to the previous queue and if that is the AWT queue it will start dispatching events immediately. This has to be avoided.
		}
		
	}

}
