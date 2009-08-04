package sneer.bricks.hardwaresharing.files.cache.impl;

import static sneer.foundation.environments.Environments.my;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import sneer.bricks.hardwaresharing.files.cache.FileCache;
import sneer.bricks.hardwaresharing.files.hasher.Hasher;
import sneer.bricks.hardwaresharing.files.protocol.FolderContents;
import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.bricks.pulp.events.EventNotifier;
import sneer.bricks.pulp.events.EventNotifiers;
import sneer.bricks.pulp.events.EventSource;

class FileCacheImpl implements FileCache {
	
	
	private final Map<Sneer1024, Object> _contents = new ConcurrentHashMap<Sneer1024, Object>();
	private final EventNotifier<FolderContents> _foldersAdded = my(EventNotifiers.class).newInstance();

	
	@Override
	public Sneer1024 putFileContents(byte[] contents) {
		Sneer1024 hash = my(Hasher.class).hashFile(contents);
		_contents.put(hash, contents);
		return hash; 
	}


	@Override
	public Sneer1024 putFolderContents(FolderContents contents) {
		Sneer1024 hash = my(Hasher.class).hashFolder(contents);
		_contents.put(hash, contents);
		_foldersAdded.notifyReceivers(contents);
		return hash; 
	}


	@Override
	public Object getContents(Sneer1024 hash) {
		return _contents.get(hash);
	}
	
	
	@Override
	public EventSource<FolderContents> foldersAdded() {
		return _foldersAdded.output();
	}

}