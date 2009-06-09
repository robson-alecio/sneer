package sneer.bricks.pulp.datastructures.cache;

import sneer.foundation.brickness.Brick;

@Brick
public interface CacheFactory {

	<T> Cache<T> createWithCapacity(int capacity);

}
