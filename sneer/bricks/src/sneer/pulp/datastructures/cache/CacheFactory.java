package sneer.pulp.datastructures.cache;

public interface CacheFactory {

	<T> Cache<T> createWithCapacity(int capacity);

}
