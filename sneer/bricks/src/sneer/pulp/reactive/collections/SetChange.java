/**
 * 
 */
package sneer.pulp.reactive.collections;

import java.util.Collection;

public interface SetChange<E> {
	Collection<E> elementsAdded();
	Collection<E> elementsRemoved();
}