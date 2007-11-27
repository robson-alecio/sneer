package wheel.reactive.maps.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import wheel.lang.Omnivore;
import wheel.lang.Pair;
import wheel.reactive.impl.AbstractNotifier;
import wheel.reactive.maps.MapSignal;
import wheel.reactive.maps.MapSource;
import wheel.reactive.maps.MapValueChange;

public class MapSourceImpl<K,V> implements MapSource<K,V> {
	
	private class MyOutput extends AbstractNotifier<MapValueChange<K,V>> implements MapSignal<K,V> {

		@Override
		public V currentGet(K key) {
			return _map.get(key);
		}

		@Override
		public int currentSize() {
			return _map.size();
		}

		@Override
		public void addMapReceiver(Omnivore<MapValueChange<K,V>> receiver) {
			addReceiver(receiver);		
		}
		
		@Override
		public void removeMapReceiver(Omnivore<MapValueChange<K,V>> receiver) {
			removeReceiver(receiver);		
		}

		@Override
		protected void initReceiver(Omnivore<MapValueChange<K,V>> receiver) {}

		@Override
		protected void notifyReceivers(MapValueChange<K,V> valueChange) {
			super.notifyReceivers(valueChange);
		}

	}

	private final Map<K,V> _map = new HashMap<K,V>();
	private MyOutput _output = new MyOutput();
	
	public void put(K key, V value){
		synchronized (_map){
			V old = _map.get(key);
			if (old!=null)
				_output.notifyReceivers(new MapElementToBeReplaced<K,V>(key,old));
			_map.put(key, value);
			if (old==null)
				_output.notifyReceivers(new MapElementAdded<K,V>(key,value));
			else
				_output.notifyReceivers(new MapElementReplaced<K,V>(key,value));
		}
	}
	
	public boolean remove(K key) {
		synchronized (_map) {
			V old = _map.get(key);
			if (old == null)
				return false;
			_output.notifyReceivers(new MapElementToBeRemoved<K,V>(key,old));
			_map.remove(key);
			_output.notifyReceivers(new MapElementToBeRemoved<K,V>(key,old));
			return true;
		}
	}
	
	public V get(K key) {
		synchronized (_map) {
			return _map.get(key);
		}
	}
	
	public Set<K> keys(){
		synchronized (_map) {
			return _map.keySet();
		}
	}

	public MapSignal<K,V> output() {
		return _output;
	}

	@Override
	public Omnivore<Pair<K,V>> setter() {
		return new Omnivore<Pair<K,V>>() { @Override public void consume(Pair<K,V> pair) {
			put(pair._a,pair._b);
		}};
	}

	private static final long serialVersionUID = 1L;
}
