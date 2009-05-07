package sneer.pulp.reactive.collections;

import java.util.Collection;

public interface CollectionChange<E> {
	
	Collection<E> elementsAdded();
	Collection<E> elementsRemoved();

}