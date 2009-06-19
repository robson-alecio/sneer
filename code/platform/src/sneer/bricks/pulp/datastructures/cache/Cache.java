package sneer.bricks.pulp.datastructures.cache;

public interface Cache<T> {
	void keep(T object);

	boolean contains(T object);
	int handleFor(T object);
	T getByHandle(int handle);
}
