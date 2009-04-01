package sneer.pulp.datastructures.cache;

import sneer.brickness.OldBrick;

public interface CacheFactory extends OldBrick {

	<T> Cache<T> createWithCapacity(int capacity);

}
