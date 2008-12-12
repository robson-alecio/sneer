package wheel.io.ui;

import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.Toolkit;

import wheel.lang.Environments;
import wheel.lang.Timebox;
import wheel.lang.Environments.Memento;

public class TimeboxedEventQueue extends EventQueue {

	
	private static TimeboxedEventQueue _singleton;

	
	public static void startQueueing(int timeboxDuration) {
		if (_singleton != null) throw new IllegalStateException();
		_singleton = new TimeboxedEventQueue(timeboxDuration);
		
		Toolkit.getDefaultToolkit().getSystemEventQueue().push(_singleton);
	}

	public static void stopQueueing() {
		_singleton.pop();
	}


	private TimeboxedEventQueue(int timeboxDuration) {
		_timeboxDuration = timeboxDuration;
		_environment = Environments.memento();
	}

	
	private final int _timeboxDuration;
	private final Memento _environment;
	
	
	@Override
	protected void dispatchEvent(final AWTEvent event) {
		new AWTEventTimebox(_timeboxDuration, event);
	}

	private void doDispatchEvent(AWTEvent event) {
		super.dispatchEvent(event);
	}
	
	
	private class AWTEventTimebox extends Timebox {
		
		private AWTEventTimebox(int timeboxDuration, AWTEvent event) {
			super(timeboxDuration, false);
			_event = event;
			Environments.runWith(_environment, this);
		}

		
		private final AWTEvent _event;


		@Override 
		protected void runInTimebox() {
			doDispatchEvent(_event);
		}
		
	}

}
