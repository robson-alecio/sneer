package sneer.pulp.reactive.collections;

import java.util.Collection;

import sneer.pulp.events.EventSource;
import sneer.pulp.reactive.Signal;

public interface CollectionSignal<T> extends EventSource<CollectionChange<T>>, Iterable<T>{

	Signal<Integer> size();

	Collection<T> currentElements();

}