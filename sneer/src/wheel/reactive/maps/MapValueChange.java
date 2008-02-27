package wheel.reactive.maps;

public interface MapValueChange<K,V> {
	
	void accept(Visitor<K,V> visitor);
	
	public interface Visitor<K,V> {
		void elementAdded(K key, V value);
		void elementToBeRemoved(K key, V value);
		void elementRemoved(K key, V value);
		void elementToBeReplaced(K key, V value);
		void elementReplaced(K key, V value);
	}
}