package wheel.io.ui;

import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.Toolkit;

import wheel.io.Logger;
import wheel.lang.exceptions.TimeIsUp;

public class TimeboxedEventQueue extends EventQueue {

	private static TimeboxedEventQueue _singleton;
	private  QueueTimebox timebox = new QueueTimebox();

	private TimeboxedEventQueue() {}

	@Override
	protected void dispatchEvent(final AWTEvent event) {
		timebox.setEventAndRun(event);
	}
	
	public static void startQueueing(){
		if(_singleton!=null) return;
		_singleton = new TimeboxedEventQueue();
		Toolkit.getDefaultToolkit().getSystemEventQueue().push(_singleton);
	}
	
	private final class QueueTimebox extends AbstractGuiTimebox {
		
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
