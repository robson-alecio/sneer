package sneer.bricks.hardware.ram.maps.cachemaps;

import sneer.foundation.brickness.Brick;

@Brick
public interface CacheMaps {

	<K, V> CacheMap<K, V> newInstance();

}
