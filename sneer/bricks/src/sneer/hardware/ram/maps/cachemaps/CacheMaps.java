package sneer.hardware.ram.maps.cachemaps;

import sneer.brickness.Brick;

@Brick
public interface CacheMaps {

	<K, V> CacheMap<K, V> newInstance();

}
