package sneer.pulp.datastructures.cache;

public interface Cache {
	void keep(Object object);
	boolean contains(Object obj);

	int handleFor(Object object);
	Object getByHandle(int handle);
}
