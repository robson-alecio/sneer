package spikes.sandro.eventQueue;

import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.Toolkit;

import wheel.lang.Timebox;

public class TimeboxedEventQueue extends EventQueue {

	private final int TIMEOUT = 3000;
	private static TimeboxedEventQueue _singleton;

	private TimeboxedEventQueue() {}

	@Override
	protected void dispatchEvent(final AWTEvent event) {
		new Timebox(TIMEOUT) { @Override protected void runInTimebox() {
			TimeboxedEventQueue.super.dispatchEvent(event);
		}};
	}
	
	public static void startQueueing(){
		if(_singleton!=null) return;
		_singleton = new TimeboxedEventQueue();
		Toolkit.getDefaultToolkit().getSystemEventQueue().push(_singleton);
	}
}
