package sneer.bricks.hardwaresharing.files.cache.impl;

import static sneer.foundation.environments.Environments.my;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import sneer.bricks.hardwaresharing.files.cache.FileCache;
import sneer.bricks.hardwaresharing.files.hasher.Hasher;
import sneer.bricks.hardwaresharing.files.protocol.FolderContents;
import sneer.bricks.pulp.crypto.Sneer1024;

class FileCacheImpl implements FileCache {
	
	
	private final static Map<Sneer1024, Object> _cache = new ConcurrentHashMap<Sneer1024, Object>();

	
	@Override
	public Sneer1024 putFileContents(byte[] contents) {
		Sneer1024 hash = my(Hasher.class).hashFile(contents);
		_cache.put(hash, contents);
		return hash; 
	}

	
	@Override
	public Object getContents(Sneer1024 hash) {
		return _cache.get(hash);
	}


	@Override
	public Sneer1024 putFolderContents(FolderContents contents) {
		Sneer1024 hash = my(Hasher.class).hashFolder(contents);
		_cache.put(hash, contents);
		return hash; 
	}

}