package wheel.io.ui;

import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.Toolkit;

import wheel.lang.Environment;
import wheel.lang.Timebox;
import wheel.lang.Environment.Provider;

public class TimeboxedEventQueue extends EventQueue {

	private static TimeboxedEventQueue _singleton;

	public static void startQueueing(Environment.Provider environment, int timeboxDuration) {
		if (_singleton != null) throw new IllegalStateException();
		_singleton = new TimeboxedEventQueue(environment, timeboxDuration);
		
		Toolkit.getDefaultToolkit().getSystemEventQueue().push(_singleton);
	}
	public static void stopQueueing() {
		_singleton.pop();
	}


	private TimeboxedEventQueue(Provider environment, int timeboxDuration) {
		_environment = environment;
		_timebox = new QueueTimebox(timeboxDuration);
	}

	private final QueueTimebox _timebox;
	private final Provider _environment;
	
	@Override
	protected void dispatchEvent(final AWTEvent event) {
		_timebox.dispatch(event);
	}
	
	
	private final class QueueTimebox extends Timebox {
		
		private QueueTimebox(int timeboxDuration) {
			super(timeboxDuration, false);
		}

		AWTEvent _event;

		
		private void dispatch(AWTEvent event) {
			if (isAlreadyInTimebox()) {
				doDispatchEvent(event);
				return;
			}

			_event = event;
//			this.run();
			Environment.runWith(_environment, this);
		}

		@Override 
		protected void runInTimebox() {
			doDispatchEvent(_event);
		}
		
		private void doDispatchEvent(AWTEvent event) {
			TimeboxedEventQueue.super.dispatchEvent(event);
		}
		

		private boolean isAlreadyInTimebox() {
			return workerThread() == Thread.currentThread();
		}
	}
}
