package sneer.bricks.hardwaresharing.files.server.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import sneer.bricks.hardware.io.IO;
import sneer.bricks.hardware.ram.arrays.ImmutableArrays;
import sneer.bricks.hardwaresharing.files.hasher.Hasher;
import sneer.bricks.hardwaresharing.files.protocol.FolderContents;
import sneer.bricks.hardwaresharing.files.protocol.FolderEntry;
import sneer.bricks.pulp.crypto.Sneer1024;

class FolderStructureCache {
	
	
	private final static Map<Sneer1024, Object> _cache = new ConcurrentHashMap<Sneer1024, Object>();
	
	
	static Sneer1024 cache(File fileOrFolder) throws IOException {
		return (fileOrFolder.isDirectory())
			? cacheFolderContents(fileOrFolder)
			: cacheFile(fileOrFolder);
	}

	
	static Object getContents(Sneer1024 hashOfContents) throws IOException {
		Object result = _cache.get(hashOfContents);
		if (result == null) return result;
		return result instanceof FolderContents
			? result
			: readFileContents((File)result);
	}

	
	private static Sneer1024 cacheFile(File file) throws IOException {
		final Sneer1024 hash = my(Hasher.class).hashFile(readFileContents(file));
		_cache.put(hash, file);
		return hash; 
	}
	
	
	private static Sneer1024 cacheFolderContents(File folder) throws IOException {
		List<FolderEntry> entries = cacheEachFolderEntry(folder);
		final FolderContents contents = new FolderContents(my(ImmutableArrays.class).newImmutableArray(entries));
		final Sneer1024 hash = my(Hasher.class).hashFolder(contents);
		_cache.put(hash, contents);
		return hash;
	}

	
	private static List<FolderEntry> cacheEachFolderEntry(File folder) throws IOException {
		List<FolderEntry> result = new ArrayList<FolderEntry>();
		
		for (File fileOrFolder : listFiles(folder))
			result.add(cacheFolderEntry(fileOrFolder));

		return result;
	}

	
	private static FolderEntry cacheFolderEntry(File fileOrFolder) throws IOException {
		Sneer1024 hashOfContents = cache(fileOrFolder);
		
		return new FolderEntry(
			fileOrFolder.getName(),
			fileOrFolder.lastModified(),
			hashOfContents
		);
	}
	
		
	private static byte[] readFileContents(File file) throws IOException {
		return my(IO.class).files().readBytes(file);
	}

	
	private static File[] listFiles(File folder) {
		File[] result = folder.listFiles();
		return result == null
			? new File[0]
			: result;
	}

}