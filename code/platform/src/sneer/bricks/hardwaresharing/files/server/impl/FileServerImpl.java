package sneer.bricks.hardwaresharing.files.server.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import sneer.bricks.hardware.cpu.lang.contracts.WeakContract;
import sneer.bricks.hardware.io.IO;
import sneer.bricks.hardware.ram.arrays.ImmutableArrays;
import sneer.bricks.hardwaresharing.files.server.FileContents;
import sneer.bricks.hardwaresharing.files.server.FileRequest;
import sneer.bricks.hardwaresharing.files.server.FileServer;
import sneer.bricks.hardwaresharing.files.server.FolderContents;
import sneer.bricks.hardwaresharing.files.server.FolderEntry;
import sneer.bricks.pulp.crypto.Crypto;
import sneer.bricks.pulp.crypto.Sneer1024;
import sneer.bricks.pulp.tuples.TupleSpace;
import sneer.foundation.lang.Consumer;

public class FileServerImpl implements FileServer {
	
	@SuppressWarnings("unused") private final WeakContract _fileRequestContract;
	{
		my(TupleSpace.class).keep(FileRequest.class);
		_fileRequestContract = my(TupleSpace.class).addSubscription(FileRequest.class, new Consumer<FileRequest>() { @Override public void consume(FileRequest request) {
			final Object fileOrFolder = _cache.get(request.hashOfContents);
			if (null != fileOrFolder) publishContents(fileOrFolder);
		}});
	}
	
	private final Map<Sneer1024, Object> _cache = new ConcurrentHashMap<Sneer1024, Object>();
	
	@Override
	public Sneer1024 serve(File fileOrFolder) throws IOException {
		return cacheFileOrFolder(fileOrFolder);
	}

	Sneer1024 cacheFileOrFolder(File fileOrFolder) throws IOException {
		
		final Sneer1024 hash = (fileOrFolder.isDirectory())
			? cacheFolderContents(fileOrFolder)
			: cacheFile(fileOrFolder);
			
		return hash;
	}

	private Sneer1024 cacheFile(File file)	throws IOException {
//		my(TupleSpace.class).publish(newDataBlock(file));
		final Sneer1024 hash = my(Crypto.class).digest(file);
		_cache.put(hash, file);
		return hash; 
	}
	
	
	private Sneer1024 cacheFolderContents(File folder) throws IOException {
		List<FolderEntry> files = cacheEachFolderEntry(folder);
		final FolderContents contents = new FolderContents(my(ImmutableArrays.class).newImmutableArray(files));
//		my(TupleSpace.class).publish(contents);
		final Sneer1024 hash = Hasher.hashFolder(contents);
		_cache.put(hash, contents);
		return hash;
	}
	
	private List<FolderEntry> cacheEachFolderEntry(File folder) throws IOException {
		List<FolderEntry> result = new ArrayList<FolderEntry>();
		
		for (File fileOrFolder : listFiles(folder))
			result.add(folderEntryFor(fileOrFolder));

		return result;
	}

	
	private FolderEntry folderEntryFor(File fileOrFolder) throws IOException {
		Sneer1024 hashOfContents = cacheFileOrFolder(fileOrFolder);
		
		return new FolderEntry(
			fileOrFolder.getName(),
			fileOrFolder.lastModified(),
			hashOfContents
		);
	}
	
	protected void publishContents(Object fileOrFolder) {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet(); // Implement
	}
	
	private FileContents newDataBlock(File fileOrFolder) throws IOException {
		byte[] bytes = my(IO.class).files().readBytes(fileOrFolder);
		return new FileContents(my(ImmutableArrays.class).newImmutableByteArray(bytes));
	}
	
	private File[] listFiles(File folder) {
		File[] result = folder.listFiles();
		return result == null
			? new File[0]
			: result;
	}
}