package sneer.pulp.reactive.collections;



public interface ReactiveCollections {

	<T> ListRegister<T> newListRegister();
	<K, V> MapRegister<K, V> newMapRegister();
	
}
