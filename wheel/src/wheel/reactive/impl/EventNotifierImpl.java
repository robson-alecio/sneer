package wheel.reactive.impl;

import wheel.lang.Omnivore;
import wheel.reactive.EventNotifier;
import wheel.reactive.EventSource;

public class EventNotifierImpl<VO> implements EventNotifier<VO> {

	static class MyOutput<VO> extends AbstractNotifier<VO> {

		@Override
		protected void initReceiver(Omnivore<? super VO> receiver) {	
			//EventNotifiers dont need to init receivers like Signals do.
		}
		
	}
	
	private final MyOutput<VO> _output = new MyOutput<VO>();
	
	@Override
	public void notifyReceivers(VO event) {
		_output.notifyReceivers(event);
	}

	@Override
	public EventSource<VO> output() {
		return _output;
	}

}
