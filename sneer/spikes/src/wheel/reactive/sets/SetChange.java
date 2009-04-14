/**
 * 
 */
package wheel.reactive.sets;

import java.util.Collection;

public interface SetChange<E> {
	Collection<E> elementsAdded();
	Collection<E> elementsRemoved();
}