package sneer.pulp.datastructures.cache;

import sneer.brickness.Brick;

public interface CacheFactory extends Brick {

	<T> Cache<T> createWithCapacity(int capacity);

}
