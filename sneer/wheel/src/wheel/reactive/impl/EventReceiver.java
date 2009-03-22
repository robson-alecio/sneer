package wheel.reactive.impl;

import java.util.ArrayList;
import java.util.List;

import sneer.pulp.events.EventSource;

import wheel.lang.Consumer;

/** Instances of this class hold a reference to the EventSources they are receiving, so that these sources are not GCd. */
public abstract class EventReceiver<T> implements Consumer<T> {
	
	private final List<EventSource<? extends T>> _sources = new ArrayList<EventSource<? extends T>>();

	public EventReceiver(EventSource<? extends T> eventSource) {
		connectTo(eventSource);
	}
	
	public EventReceiver(EventSource<? extends T>... eventSource) {
		for (EventSource<? extends T> source : eventSource)
			connectTo(source);
	}

	private void connectTo(EventSource<? extends T> eventSource) {
		if (eventSource == null) throw new IllegalArgumentException();
		if (_sources.add(eventSource))
			eventSource.addReceiver(this);
	}
	
}
