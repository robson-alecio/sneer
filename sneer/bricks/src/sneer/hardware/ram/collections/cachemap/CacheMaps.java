package sneer.hardware.ram.collections.cachemap;

import sneer.brickness.Brick;

@Brick
public interface CacheMaps {

	<K, V> CacheMap<K, V> newInstance();

}
