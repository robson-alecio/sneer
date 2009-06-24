package sneer.bricks.hardware.gui.timebox.impl;

import static sneer.foundation.environments.Environments.my;

import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import sneer.bricks.hardware.cpu.timebox.Timebox;
import sneer.bricks.hardware.gui.timebox.TimeboxedEventQueue;
import sneer.bricks.hardware.io.log.Logger;
import sneer.foundation.environments.Environment;
import sneer.foundation.environments.EnvironmentUtils;
import sneer.foundation.environments.Environments;

class TimeboxedEventQueueImpl extends EventQueue implements TimeboxedEventQueue {

	private static Environment _environment;
	private static int _timeboxDuration;

	private static List<TimeboxedEventQueueImpl> _queues = new ArrayList<TimeboxedEventQueueImpl>();

	@Override
	public synchronized void startQueueing(int timeboxDuration) {
		if (!_queues.isEmpty()) throw new IllegalStateException("Queueing already started.");

		_environment = my(Environment.class);
		_timeboxDuration = timeboxDuration;
		
		startNewQueue();
	}

	@Override
	public synchronized void stopQueueing() {
		for (TimeboxedEventQueueImpl queue : _queues) queue.pop();
		_queues.clear();
		_environment = null;
	}

	private static void startNewQueue() {
		TimeboxedEventQueueImpl newQueue = new TimeboxedEventQueueImpl();
		_queues.add(0, newQueue);
		Toolkit.getDefaultToolkit().getSystemEventQueue().push(newQueue);
	}

	@Override
	protected void dispatchEvent(final AWTEvent event) {
		Runnable timebox = EnvironmentUtils.retrieveFrom(_environment, Timebox.class).prepare(_timeboxDuration, new Runnable(){ @Override public void run() {
			superDispatchEvent(event);
		}}, new Runnable(){ @Override public void run() {
			my(Logger.class).log("Starting new Gui Thread");
			
			startNewQueue(); //This is an EventQueue leak. Not serious compared to the thread leak of the blocked threads that make these new EventQueues necessary. If these EventQueues are ever discarded (popped), take care because pending events will be automatically passed on to the previous queue and if that is the AWT queue it will start dispatching events immediately. This has to be avoided.
		}});
		
		Environments.runWith(_environment, timebox);
	}
	
	private void superDispatchEvent(final AWTEvent event) {
		TimeboxedEventQueueImpl.super.dispatchEvent(event);
	}

}
