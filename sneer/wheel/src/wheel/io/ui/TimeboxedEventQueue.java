package wheel.io.ui;

import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.Toolkit;

import wheel.io.Logger;
import wheel.lang.Timebox;
import wheel.lang.exceptions.TimeIsUp;

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
	}

	
	private final QueueTimebox _timebox = new QueueTimebox();
	private final int _timeboxDuration;

	
	@Override
	protected void dispatchEvent(final AWTEvent event) {
		_timebox.setEventAndRun(event);
	}
	
	
	private final class QueueTimebox extends Timebox {
		
		private QueueTimebox() {
			super(_timeboxDuration, false);
		}

		AWTEvent _event;

		@Override 
		protected void runInTimebox() {
			TimeboxedEventQueue.super.dispatchEvent(_event);
		}
		
		private void setEventAndRun(AWTEvent event){
			_event = event;
			try {
				this.run();
			} catch (TimeIsUp timeIsUp) {
				Logger.log(timeIsUp);
			}
		}
	}
}
