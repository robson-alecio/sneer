package sneer.bricks.pulp.reactive.collections;

public interface MapValueChange<K,V> {
	
	void accept(Visitor<K,V> visitor);
	
	public interface Visitor<K,V> {
		void entryAdded(K key, V value);
		void entryRemoved(K key, V value);
		void entryReplaced(K key, V value);
	}
}