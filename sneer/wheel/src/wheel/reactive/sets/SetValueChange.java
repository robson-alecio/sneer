/**
 * 
 */
package wheel.reactive.sets;

import java.util.Collection;

public interface SetValueChange<E> {
	Collection<E> elementsAdded();
	Collection<E> elementsRemoved();
}