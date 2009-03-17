package wheel.reactive.impl;

import sneer.pulp.events.EventNotifier;
import sneer.pulp.events.EventSource;
import wheel.lang.Consumer;

public class EventNotifierImpl<VO> implements EventNotifier<VO> {

	static class MyOutput<VO> extends AbstractNotifier<VO> {

		@Override
		protected void initReceiver(Consumer<? super VO> receiver) {	
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
