package sneer.pulp.reactive.collections.listfilter;

public interface Filter<T> {

	boolean select(T element);
}
