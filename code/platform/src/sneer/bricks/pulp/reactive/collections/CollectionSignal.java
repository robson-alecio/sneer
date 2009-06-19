package sneer.bricks.pulp.reactive.collections;

import java.util.Collection;

import sneer.bricks.pulp.events.EventSource;
import sneer.bricks.pulp.reactive.Signal;

public interface CollectionSignal<T> extends EventSource<CollectionChange<T>>, Iterable<T>{

	Signal<Integer> size();

	Collection<T> currentElements();

}