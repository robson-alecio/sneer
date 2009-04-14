package wheel.io.ui;

import static sneer.commons.environments.Environments.my;

import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import sneer.commons.environments.Environment;
import sneer.commons.environments.Environments;
import sneer.hardware.cpu.timebox.Timebox;
import wheel.io.Logger;

public class TimeboxedEventQueue extends EventQueue {

	private static Environment _environment;
	private static int _timeboxDuration;

	private static List<TimeboxedEventQueue> _queues = new ArrayList<TimeboxedEventQueue>();

	
	static public synchronized void startQueueing(int timeboxDuration) {
		if (!_queues.isEmpty()) throw new IllegalStateException("Queueing already started.");

		_environment = my(Environment.class);
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
		Runnable timebox = _environment.provide(Timebox.class).prepare(_timeboxDuration, new Runnable(){ @Override public void run() {
			superDispatchEvent(event);
		}}, new Runnable(){ @Override public void run() {
			Logger.log("Starting new Gui Thread");
			
			startNewQueue(); //This is an EventQueue leak. Not serious compared to the thread leak of the blocked threads that make these new EventQueues necessary. If these EventQueues are ever discarded (popped), take care because pending events will be automatically passed on to the previous queue and if that is the AWT queue it will start dispatching events immediately. This has to be avoided.
		}});
		
		Environments.runWith(_environment, timebox);
	}
	
	private void superDispatchEvent(final AWTEvent event) {
		TimeboxedEventQueue.super.dispatchEvent(event);
	}

}
