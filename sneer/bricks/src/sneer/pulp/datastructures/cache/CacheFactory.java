package sneer.pulp.datastructures.cache;

import sneer.kernel.container.Brick;

public interface CacheFactory extends Brick {

	<T> Cache<T> createWithCapacity(int capacity);

}
