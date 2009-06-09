package sneer.pulp.datastructures.cache;

import sneer.brickness.Brick;

@Brick
public interface CacheFactory {

	<T> Cache<T> createWithCapacity(int capacity);

}
